package com.woowacourse.smody.push.strategy;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.repository.PushSubscriptionRepository;

class SubscriptionPushStrategyTest extends IntegrationTest {

	@Autowired
	@InjectMocks
	private SubscriptionPushStrategy pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private PushSubscriptionRepository pushSubscriptionRepository;

	@DisplayName("알림 구독에 대한 알림을 전송한다.")
	@Test
	void push() {
		// given
		Member member = fixture.회원_조회(조조그린_ID);
		PushSubscription pushSubscription = new PushSubscription("endpoint", "p245dh", "auth", member);
		pushSubscriptionRepository.save(pushSubscription);

		// when
		pushStrategy.push(pushSubscription);

		// then
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getMessage()).contains("스모디 알림이 구독되었습니다."),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.SUBSCRIPTION),
			() -> assertThat(pushNotification.getPathId()).isNull()
		);
	}
}