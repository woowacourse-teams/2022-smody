package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.support.IntegrationTest;

class SubscriptionPushEventIntegrationTest extends IntegrationTest {

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private PushSubscriptionService pushSubscriptionService;

	@DisplayName("알림을 구독하면 푸시 알람 내역이 발송된 상태로 저장된다.")
	@Test
	void subscribe_pushNotification() throws InterruptedException {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		// when
		synchronize(() -> pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest));

		// then
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("조조그린님 스모디 알림이 구독되었습니다."),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.SUBSCRIPTION),
			() -> assertThat(pushNotification.getPathId()).isNull()
		);
	}
}
