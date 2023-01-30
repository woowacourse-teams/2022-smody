package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.service.CommentApiService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.support.EventListenerMockTest;

class CommentPushEventListenerIntegrationTest extends EventListenerMockTest {

    @Autowired
    private CommentApiService commentApiService;

    @DisplayName("댓글을 작성하면 알림이 저장 된다.")
    @Test
    void createComment_push() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        CommentRequest commentRequest = new CommentRequest("댓글입니다");

        // when
        commentApiService.create(new TokenPayload(더즈_ID), cycleDetail.getId(), commentRequest);

        // then
        verify(commentPushEventListener)
            .handle(any(CommentCreateEvent.class));
    }

    @DisplayName("자신의 댓글을 작성하면 알림이 저장되지 않는다.")
    @Test
    void createComment_notPush() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        CommentRequest commentRequest = new CommentRequest("댓글입니다");

        // when
        commentApiService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(), commentRequest);

        // then
        verify(commentPushEventListener)
            .handle(any(CommentCreateEvent.class));
    }
}
