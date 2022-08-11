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
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

@SpringBootTest
@Transactional
class PushSchedulerTest {

	@Autowired
	@InjectMocks
	private PushScheduler pushScheduler;

	@MockBean
	private WebPushService webPushService;

	@Autowired
	private ResourceFixture fixture;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private PushSubscriptionService pushSubscriptionService;

	private Member member1;
	private Member member2;

	@BeforeEach
	void init() {
		member1 = fixture.회원_조회(1L);
		member2 = fixture.회원_조회(2L);

		pushSubscriptionService.subscribe(
			new TokenPayload(조조그린_ID),
			new SubscriptionRequest("endpoint1", "p256dh", "auth")
		);

		pushSubscriptionService.subscribe(
			new TokenPayload(더즈_ID),
			new SubscriptionRequest("endpoint2", "p256dh", "auth")
		);

		LocalDateTime now = LocalDateTime.now();

		pushNotificationRepository.save(new PushNotification(
			"알림", now.minusHours(1L), PushStatus.IN_COMPLETE, member1
		));

		pushNotificationRepository.save(new PushNotification(
			"알림", now.plusHours(1L), PushStatus.IN_COMPLETE, member2
		));
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
		assertThat(result).hasSize(3);
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
