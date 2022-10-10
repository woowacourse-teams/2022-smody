package com.woowacourse.smody.push.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushSubscriptionServiceTest extends IntegrationTest {

    @Autowired
    private PushSubscriptionService pushSubscriptionService;

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
