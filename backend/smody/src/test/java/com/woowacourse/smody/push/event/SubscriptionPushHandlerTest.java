package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

@SpringBootTest
@Transactional
class SubscriptionPushHandlerTest {

	@Autowired
	@InjectMocks
	private PushSubscriptionService pushSubscriptionService;

	@MockBean
	private WebPushService webPushService;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@DisplayName("알림을 구독하면 푸시 알람 내역이 발송된 상태로 저장된다.")
	@Test
	void subscribe_pushNotification() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		// when
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);

		// then
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE)
		);
	}

	@DisplayName("구독 알림이 요청될 때 예외가 발생하면 비즈니스 예외로 변환한다.")
	@Test
	void subscribe_libraryException() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");
		doThrow(new BusinessException(ExceptionData.WEB_PUSH_ERROR))
			.when(webPushService).sendNotification(any(), any());

		// when // then
		assertThatThrownBy(() -> pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest))
			.isInstanceOf(BusinessException.class)
			.extracting("exceptionData")
			.isEqualTo(ExceptionData.WEB_PUSH_ERROR);
	}

}
