package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushNotificationServiceTest extends IntegrationTest {

	@Autowired
	private PushNotificationService pushNotificationService;

	@DisplayName("회원의 발송된 알림을 시간 내림차순으로 모두 가져온다.")
	@Test
	void findByMember() {
		// given
		LocalDateTime now = LocalDateTime.now();

		PushNotification notification1 = fixture.발송된_알림_생성(
			조조그린_ID, null, now.minusHours(2L), PushCase.SUBSCRIPTION
		);
		PushNotification notification2 = fixture.발송된_알림_생성(
			조조그린_ID, 1L, now.minusHours(1L), PushCase.CHALLENGE
		);
		PushNotification notification3 = fixture.발송된_알림_생성(
			조조그린_ID, 2L, now, PushCase.CHALLENGE
		);

		fixture.발송된_알림_생성(더즈_ID, 3L, now.minusHours(3L), PushCase.CHALLENGE);
		fixture.발송_예정_알림_생성(
			조조그린_ID, 2L, now.plusHours(1L), PushCase.CHALLENGE
		);

		// when
		List<PushNotificationResponse> responses = pushNotificationService.searchNotificationsOfMine(
			new TokenPayload(조조그린_ID));

		// then
		assertAll(
			() -> assertThat(responses).hasSize(3),
			() -> assertThat(responses)
				.map(PushNotificationResponse::getPushNotificationId)
				.containsExactly(notification3.getId(), notification2.getId(), notification1.getId()),
			() -> assertThat(responses)
				.map(PushNotificationResponse::getPushCase)
				.containsExactly("challenge", "challenge", "subscription")
		);
	}

	@DisplayName("알림을 삭제한다.")
	@Test
	void delete() {
		// given
		PushNotification notification = fixture.발송된_알림_생성(
			조조그린_ID, null, LocalDateTime.now().minusHours(2L), PushCase.SUBSCRIPTION
		);

		// when
		pushNotificationService.delete(notification.getId());

		// then
		List<PushNotificationResponse> responses = pushNotificationService.searchNotificationsOfMine(
			new TokenPayload(조조그린_ID));
		assertThat(responses).isEmpty();
	}
}
