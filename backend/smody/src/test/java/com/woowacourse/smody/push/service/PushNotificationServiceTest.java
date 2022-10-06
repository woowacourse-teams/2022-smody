package com.woowacourse.smody.push.service;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.push.dto.PushNotificationResponse;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;

class PushNotificationServiceTest extends IntegrationTest {

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

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

	@DisplayName("알람을 저장한다.")
	@Test
	void saveNotification() {
		// given
		LocalDateTime now = LocalDateTime.now();
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
		List<Long> ids = List.of(토닉_ID, 더즈_ID);
		MentionNotificationRequest mentionNotificationRequest =
				new MentionNotificationRequest(ids, cycleDetail.getId());

		// when
		pushNotificationService.saveNotification(tokenPayload, mentionNotificationRequest);

		// then
		PushNotification pushNotification1 = pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE).get(0);
		PushNotification pushNotification2 = pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE).get(1);
		assertAll(
				() -> assertThat(pushNotification1.getMember().getId()).isEqualTo(더즈_ID),
				() -> assertThat(pushNotification1.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
				() -> assertThat(pushNotification1.getMessage()).isEqualTo("조조그린님께서 회원님을 언급하셨습니다!"),
				() -> assertThat(pushNotification1.getPushCase()).isEqualTo(PushCase.MENTION),
				() -> assertThat(pushNotification1.getPathId()).isEqualTo(cycleDetail.getId()),
				() -> verify(webPushService, never())
						.sendNotification(any(), any()),

				() -> assertThat(pushNotification2.getMember().getId()).isEqualTo(토닉_ID),
				() -> assertThat(pushNotification2.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
				() -> assertThat(pushNotification2.getMessage()).isEqualTo("조조그린님께서 회원님을 언급하셨습니다!"),
				() -> assertThat(pushNotification2.getPushCase()).isEqualTo(PushCase.MENTION),
				() -> assertThat(pushNotification2.getPathId()).isEqualTo(cycleDetail.getId()),
				() -> verify(webPushService, never())
						.sendNotification(any(), any())
		);
	}

	@DisplayName("발송 가능한 알림을 조회한다.")
	@Test
	void searchPushable() {
		// given
		LocalDateTime now = LocalDateTime.now();

		fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.minusMinutes(2L), PushCase.SUBSCRIPTION);
		fixture.발송_예정_알림_생성(더즈_ID, 2L, now.minusMinutes(3L), PushCase.CHALLENGE);

		fixture.발송_예정_알림_생성(조조그린_ID, 3L, now.plusMinutes(2L), PushCase.CHALLENGE);
		fixture.발송된_알림_생성(토닉_ID, 4L, now.minusHours(1L), PushCase.CHALLENGE);

		// when
		List<PushNotification> actual = pushNotificationService.searchPushable();

		// then
		assertThat(actual)
			.map(PushNotification::getPathId)
			.containsOnly(1L, 2L);
	}

	@DisplayName("알림들을 모두 발송 완료 상태로 변경한다.")
	@Test
	void completeAll() {
		// given
		LocalDateTime now = LocalDateTime.now();

		List<PushNotification> notifications = List.of(
			fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.minusMinutes(2L), PushCase.SUBSCRIPTION),
			fixture.발송_예정_알림_생성(더즈_ID, 2L, now.minusMinutes(3L), PushCase.CHALLENGE),

			fixture.발송_예정_알림_생성(조조그린_ID, 3L, now.minusMinutes(2L), PushCase.CHALLENGE),
			fixture.발송_예정_알림_생성(토닉_ID, 4L, now.minusHours(1L), PushCase.CHALLENGE)
		);

		// when
		pushNotificationService.completeAll(notifications);

		// then
		assertThat(pushNotificationRepository.findAll())
			.map(PushNotification::getPushStatus)
			.containsExactly(PushStatus.COMPLETE, PushStatus.COMPLETE, PushStatus.COMPLETE, PushStatus.COMPLETE);
	}
}
