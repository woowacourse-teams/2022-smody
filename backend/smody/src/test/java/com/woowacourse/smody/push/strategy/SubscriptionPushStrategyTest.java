package com.woowacourse.smody.push.strategy;

import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubscriptionPushStrategyTest extends IntegrationTest {

	@Autowired
	private SubscriptionPushStrategy pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@DisplayName("알림 구독에 대한 알림을 전송한다.")
	@Test
	void push() {
		// given
		PushSubscription pushSubscription = fixture.알림_구독(조조그린_ID, "endpoint");


		// when
		pushStrategy.push(pushSubscription);

		// then
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getMessage()).contains("조조그린님 스모디 알림이 구독되었습니다."),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.SUBSCRIPTION),
			() -> assertThat(pushNotification.getPathId()).isNull()
		);
	}
}
