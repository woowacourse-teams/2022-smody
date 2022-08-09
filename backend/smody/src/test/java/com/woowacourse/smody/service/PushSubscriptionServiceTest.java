package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.PushSubscriptionRepository;

@SpringBootTest
@Transactional
class PushSubscriptionServiceTest {

	@Autowired
	@InjectMocks
	private PushSubscriptionService pushSubscriptionService;

	@MockBean
	private WebPushService webPushService;

	@Autowired
	private PushSubscriptionRepository pushSubscriptionRepository;

	@DisplayName("웹 푸시 알람을 구독한다.")
	@Test
	void subscribe() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		// when
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);

		// then
		Optional<PushSubscription> findSubscription = pushSubscriptionRepository.findByEndpoint("endpoint-link");
		assertThat(findSubscription)
			.map(subscription -> subscription.getMember().getId())
			.get()
			.isEqualTo(조조그린_ID);
	}

	@DisplayName("웹 푸시 라이브러리 예외가 발생하면 비즈니스 예외로 변환한다.")
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

	@DisplayName("같은 엔드포인트로 다른 회원이 알림을 구독하면 새로운 회원으로 대체된다.")
	@Test
	void subscribe_newMember() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);

		// when
		pushSubscriptionService.subscribe(new TokenPayload(더즈_ID), subscriptionRequest);

		// then
		Optional<PushSubscription> findSubscription = pushSubscriptionRepository.findByEndpoint("endpoint-link");
		assertThat(findSubscription)
			.map(subscription -> subscription.getMember().getId())
			.get()
			.isEqualTo(더즈_ID);
	}

	@DisplayName("엔드포인트로 알림 구독 정보를 삭제한다.")
	@Test
	void unsubscribe() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);
		UnSubscriptionRequest unSubscriptionRequest = new UnSubscriptionRequest("endpoint-link");

		// when
		pushSubscriptionService.unSubscribe(tokenPayload, unSubscriptionRequest);

		// then
		Optional<PushSubscription> findSubscription = pushSubscriptionRepository.findByEndpoint("endpoint-link");
		assertThat(findSubscription).isEmpty();
	}
}
