package com.woowacourse.smody.comment.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentApiServiceTest extends IntegrationTest {

    @Autowired
    private CommentApiService commentApiService;

    @Autowired
    private CommentRepository commentRepository;

    private LocalDateTime now = LocalDateTime.now();

    @DisplayName("댓글을 생성한다.")
    @Test
    void create() {
        // given
        Cycle cycle = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        Long commentId = commentApiService.create(
                new TokenPayload(조조그린_ID), cycleDetail.getId(), new CommentRequest("댓글")
        );

        // then
        assertThat(commentRepository.findById(commentId).get().getContent()).isEqualTo("댓글");
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void update() {
        // given
        Cycle cycle = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "수정전"));

        // when
        commentApiService.update(
                new TokenPayload(조조그린_ID), cycleDetail.getId(), new CommentUpdateRequest("수정후")
        );

        // then
        Optional<Comment> findComment = commentRepository.findById(comment.getId());
        assertThat(findComment.get().getContent()).isEqualTo("수정후");
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void delete() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Comment comment = new Comment(cycleDetail, cycle.getMember(), "댓글");
        commentRepository.save(comment);
        Long commentId = comment.getId();

        // when
        commentApiService.delete(new TokenPayload(조조그린_ID), commentId);

        // then
        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    @DisplayName("특정 cycleDetail 의 댓글을 조회한다.")
    @Test
    void findAllByCycle() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);
        Long comment1 = commentApiService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글1"));
        Long comment2 = commentApiService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글2"));

        // when
        List<CommentResponse> commentResponses = commentApiService.findAllByCycleDetailId(
                TokenPayload.NOT_LOGIN_TOKEN_PAYLOAD, cycleDetail.getId());

        // then
        assertThat(commentResponses.stream()
                .map(CommentResponse::getCommentId)
                .collect(Collectors.toList())).containsExactly(comment1, comment2);
    }
}
