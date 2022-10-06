package com.woowacourse.smody.push.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PushNotificationTest {

	private static final LocalDateTime NOW = LocalDateTime.now();

	@DisplayName("아직 보내면 안 되는 알림인지 판별한다.")
	@Test
	void isPushable_false() {
		// given
		PushNotification notification = PushNotification.builder()
			.message("알림")
			.pushTime(NOW.plusMinutes(1L))
			.pushStatus(PushStatus.IN_COMPLETE)
			.pushCase(PushCase.SUBSCRIPTION)
			.build();

		// when
		boolean result = notification.isPushable(NOW);

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("보내도 되는 알림인지 판별한다.")
	@Test
	void isPushable_true() {
		// given
		PushNotification notification = PushNotification.builder()
			.message("알림")
			.pushTime(NOW.minusMinutes(1L))
			.pushStatus(PushStatus.IN_COMPLETE)
			.pushCase(PushCase.SUBSCRIPTION)
			.build();

		// when
		boolean result = notification.isPushable(NOW);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("알림은 전송하면 전송 완료 상태가 된다.")
	@Test
	void completePush() {
		// given
		PushNotification notification = PushNotification.builder()
			.message("알림")
			.pushTime(NOW.minusMinutes(1L))
			.pushStatus(PushStatus.IN_COMPLETE)
			.pushCase(PushCase.SUBSCRIPTION)
			.build();

		// when
		notification.completePush();

		// then
		assertThat(notification.getPushStatus()).isEqualTo(PushStatus.COMPLETE);
	}
}
