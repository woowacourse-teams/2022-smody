package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.dto.CommentRequest;
import com.woowacourse.smody.dto.CommentUpdateRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.CommentRepository;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentServiceTest extends IntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ResourceFixture resourceFixture;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("댓글을 생성한다.")
    @Test
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        // when
        Long commentId = commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글"));

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글");
    }

    @DisplayName("댓글 생성 시 생성된 시간을 가진다.")
    @Test
    void create_withCreatedAt() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        // when
        Long commentId = commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글"));
        assertThat(commentRepository.findById(commentId).get().getCreatedAt()).isNotNull();
    }

    @DisplayName("댓글 생성 시 cycleDetail을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void create_notFoundCycleDetail() {
        assertThatThrownBy(() -> commentService.create(new TokenPayload(조조그린_ID), 1L,
                new CommentRequest("댓글")))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CYCLE_DETAIL);
    }

    @DisplayName("댓글 생성 시 회원을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void create_notFoundMember() {
        // given
        Long invalidMemberId = 0L;
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        // when, then
        assertThatThrownBy(() -> commentService.create(new TokenPayload(invalidMemberId), cycleDetail.getId(),
                new CommentRequest("댓글")))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_MEMBER);
    }

    @DisplayName("댓글 생성 시 content가 1자 미만, 255자를 초과할 경우 예외를 발생시킨다.")
    @Test
    void create_invalidCommentContent() {
        // given
        String invalidContent = "1234567890".repeat(25) + "123456";
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        // when, then
        assertThatThrownBy(() -> commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest(invalidContent)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_COMMENT_CONTENT);
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when
        commentService.update(new TokenPayload(조조그린_ID), comment.getId(),
                new CommentUpdateRequest("수정후"));

        // then
        assertThat(comment.getContent()).isEqualTo("수정후");
    }

    @DisplayName("댓글 수정 시 댓글을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void updateComment_notFound() {
        assertThatThrownBy(() -> commentService.update(new TokenPayload(조조그린_ID), 1L,
                new CommentUpdateRequest("수정후")))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_COMMENT);
    }

    @DisplayName("댓글 수정 시 멤버가 작성한 댓글이 아닌 경우 예외를 발생시킨다.")
    @Test
    void updateComment_unauthorizedMember() {
        // given
        Long unauthorizedMemberId = 0L;
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when
        assertThatThrownBy(() -> commentService.update(new TokenPayload(unauthorizedMemberId), comment.getId(),
                new CommentUpdateRequest("수정후")))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.UNAUTHORIZED_MEMBER);
    }

    @DisplayName("댓글 수정 시 1자 미만 255자 초과하는 댓글이면 예외를 발생시킨다.")
    @Test
    void updateComment_invalidContent() {
        // given
        String invalidContent = "1234567890".repeat(25) + "123456";
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when then
        assertThatThrownBy(() -> commentService.update(new TokenPayload(조조그린_ID), comment.getId(),
                new CommentUpdateRequest(invalidContent)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_COMMENT_CONTENT);
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = new Comment(cycleDetail, cycle.getMember(), "댓글");
        commentRepository.save(comment);
        Long commentId = comment.getId();

        // when
        commentService.delete(new TokenPayload(조조그린_ID), commentId);
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(commentRepository.findById(commentId).isEmpty()).isTrue();
    }

    @DisplayName("댓글 삭제 시 작성자가 아니면 예외를 발생시킨다.")
    @Test
    void delete_unauthorizedMember() {
        // given
        Long unauthorizedMemberId = 0L;
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = new Comment(cycleDetail, cycle.getMember(), "댓글");
        commentRepository.save(comment);
        Long commentId = comment.getId();

        // when then
        assertThatThrownBy(() -> commentService.delete(new TokenPayload(unauthorizedMemberId), commentId))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.UNAUTHORIZED_MEMBER);
    }

    @DisplayName("댓글 삭제 시 댓글이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void delete_notFoundComment() {
        // given
        Long notFoundCommentId = 0L;
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        assertThatThrownBy(() -> commentService.delete(new TokenPayload(조조그린_ID), notFoundCommentId))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_COMMENT);
    }
}
