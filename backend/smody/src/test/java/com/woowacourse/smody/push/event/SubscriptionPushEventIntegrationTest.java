package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.service.PushSubscriptionApiService;
import com.woowacourse.smody.support.EventListenerMockTest;

class SubscriptionPushEventIntegrationTest extends EventListenerMockTest {

    @Autowired
    private PushSubscriptionApiService pushSubscriptionApiService;

    @DisplayName("알림을 구독하면 푸시 알람 내역이 발송된 상태로 저장된다.")
    @Test
    void subscribe_pushNotification() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
                "endpoint-link", "p256dh", "auth");

        // when
        pushSubscriptionApiService.subscribe(tokenPayload, subscriptionRequest);

        // then
        BDDMockito.verify(subscriptionPushEventListener)
            .handle(any(PushSubscribeEvent.class));
    }
}
