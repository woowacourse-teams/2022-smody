package com.woowacourse.smody.push;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.PushSubscriptionService;

class PushSchedulerTest extends IntegrationTest {

    @Autowired
    private PushScheduler pushScheduler;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @Autowired
    private PushSubscriptionService pushSubscriptionService;

    private Member member1;
    private Member member2;

	@BeforeEach
	void init() {
		member1 = fixture.회원_조회(조조그린_ID);
		member2 = fixture.회원_조회(더즈_ID);

        LocalDateTime now = LocalDateTime.now();

		fixture.알림_구독(조조그린_ID, "endpoint1");
		fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.minusHours(1L), PushCase.CHALLENGE);

		fixture.알림_구독(더즈_ID, "endpoint2");
		fixture.발송_예정_알림_생성(더즈_ID, 1L, now.plusHours(1L), PushCase.SUBSCRIPTION);

		fixture.발송_예정_알림_생성(토닉_ID, 1L, now.minusHours(1L), PushCase.CHALLENGE);
	}

    @DisplayName("발송 안 된 알림들을 모두 전송한다.")
    @Test
    void sendPushNotifications() {
        // given
        BDDMockito.given(webPushService.sendNotification(any(), any()))
                .willReturn(true);

        // when
        pushScheduler.sendPushNotifications();

		// then
		List<PushNotification> result = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE);
		assertThat(result).hasSize(2);
	}

    @DisplayName("알림을 전송할 때 적절하지 않는 구독 정보는 삭제한다.")
    @Test
    void sendPushNotifications_deleteSubscriptions() {
        // given
        BDDMockito.given(webPushService.sendNotification(any(), any()))
                .willReturn(false);

        // when
        pushScheduler.sendPushNotifications();

        // then
        List<PushSubscription> subscriptions = pushSubscriptionService.searchByMembers(
                List.of(member1, member2)
        );
        assertAll(
                () -> assertThat(subscriptions).hasSize(1),
                () -> assertThat(subscriptions.get(0).getMember()).isEqualTo(member2)
        );

    }
}
