package com.woowacourse.smody.push.event;

import com.woowacourse.smody.push.domain.*;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubscriptionPushEventListenerTest extends IntegrationTest {

	@Autowired
	private SubscriptionPushEventListener pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@DisplayName("알림 구독에 대한 알림을 저장한다")
	@Test
	void push() throws InterruptedException {
		// given
		PushSubscription pushSubscription = fixture.알림_구독(조조그린_ID, "endpoint");

		// when
		synchronize(() -> pushStrategy.handle(new PushSubscribeEvent(pushSubscription)));

		// then
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getMessage()).contains("조조그린님 스모디 알림이 구독되었습니다."),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.SUBSCRIPTION),
			() -> assertThat(pushNotification.getPathId()).isNull()
		);
	}
}
