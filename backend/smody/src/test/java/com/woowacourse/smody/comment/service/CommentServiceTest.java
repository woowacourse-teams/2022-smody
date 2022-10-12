package com.woowacourse.smody.comment.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.support.IntegrationTest;
import com.woowacourse.smody.support.ResourceFixture;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentServiceTest extends IntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ResourceFixture resourceFixture;

    @DisplayName("댓글을 생성한다.")
    @Test
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        // when
        Comment comment = commentService.create(조조그린_ID, cycleDetail.getId(), "댓글");

        // then
        assertThat(commentRepository.findById(comment.getId()).get().getContent()).isEqualTo("댓글");
    }

    @DisplayName("댓글 생성 시 생성된 시간을 가진다.")
    @Test
    void create_withCreatedAt() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        // when
        Comment comment = commentService.create(조조그린_ID, cycleDetail.getId(), "댓글");

        // then
        assertThat(commentRepository.findById(comment.getId()).get().getCreatedAt()).isNotNull();
    }

    @DisplayName("댓글 생성 시 cycleDetail을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void create_notFoundCycleDetail() {
        assertThatThrownBy(() -> commentService.create(조조그린_ID, 1L, "댓글"))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        // when, then
        assertThatThrownBy(() -> commentService.create(invalidMemberId, cycleDetail.getId(), "댓글"))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        // when, then
        assertThatThrownBy(() -> commentService.create(조조그린_ID, cycleDetail.getId(), invalidContent))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when
        commentService.update(조조그린_ID, comment.getId(), "수정후");

        // then
        Optional<Comment> findComment = commentRepository.findById(comment.getId());
        assertThat(findComment.get().getContent()).isEqualTo("수정후");
    }

    @DisplayName("댓글 수정 시 댓글을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void updateComment_notFound() {
        assertThatThrownBy(() -> commentService.update(조조그린_ID, 1L, "수정후"))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when
        assertThatThrownBy(() -> commentService.update(unauthorizedMemberId, comment.getId(), "수정후"))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when then
        assertThatThrownBy(() -> commentService.update(조조그린_ID, comment.getId(), invalidContent))
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
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = new Comment(cycleDetail, cycle.getMember(), "댓글");
        commentRepository.save(comment);
        Long commentId = comment.getId();

        // when
        commentService.delete(조조그린_ID, commentId);

        // then
        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    @DisplayName("댓글 삭제 시 작성자가 아니면 예외를 발생시킨다.")
    @Test
    void delete_unauthorizedMember() {
        // given
        Long unauthorizedMemberId = 0L;
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = new Comment(cycleDetail, cycle.getMember(), "댓글");
        commentRepository.save(comment);
        Long commentId = comment.getId();

        // when then
        assertThatThrownBy(() -> commentService.delete(unauthorizedMemberId, commentId))
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
        resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);

        assertThatThrownBy(() -> commentService.delete(조조그린_ID, notFoundCommentId))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_COMMENT);
    }
}
