package com.woowacourse.smody.push.event;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.push.domain.MentionCreateEvent;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MentionPushEventListenerTest extends IntegrationTest {

    @Autowired
    private MentionPushEventListener pushStrategy;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    private CycleDetail cycleDetail;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
        cycleDetail = cycle.getCycleDetails().get(0);
    }

    @DisplayName("멘션을 남기면 멘션 대상자에게 발송 상태의 알림이 저장 된다.")
    @Test
    void push() throws InterruptedException {
        // given
        List<Long> ids = new ArrayList<>();
        ids.add(토닉_ID);

        // when
        pushStrategy.handle(new MentionCreateEvent(ids, 조조그린_ID, cycleDetail.getId()));

        taskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        // then
        PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
        assertAll(
                () -> assertThat(pushNotification.getMember().getId()).isEqualTo(토닉_ID),
                () -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
                () -> assertThat(pushNotification.getMessage()).isEqualTo("조조그린님께서 회원님을 언급하셨습니다!"),
                () -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.MENTION),
                () -> assertThat(pushNotification.getPathId()).isEqualTo(cycleDetail.getId()),
                () -> verify(webPushService, never())
                        .sendNotification(any(), any())
        );
    }

    @DisplayName("알림을 구독했으면 멘션에서 언급되었을 시 알림 저장에 전송까지 실행된다.")
    @Test
    void push_existSubscription() throws InterruptedException {
        // given
        fixture.알림_구독(토닉_ID, "endpoint");
        List<Long> ids = new ArrayList<>();
        ids.add(토닉_ID);

        // when
        pushStrategy.handle(new MentionCreateEvent(ids, 조조그린_ID, cycleDetail.getId()));

        taskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        // then
        PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
        assertAll(
                () -> assertThat(pushNotification.getMember().getId()).isEqualTo(토닉_ID),
                () -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
                () -> assertThat(pushNotification.getMessage()).isEqualTo("조조그린님께서 회원님을 언급하셨습니다!"),
                () -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.MENTION),
                () -> assertThat(pushNotification.getPathId()).isEqualTo(cycleDetail.getId()),
                () -> verify(webPushService)
                        .sendNotification(any(), any())
        );
    }
}
