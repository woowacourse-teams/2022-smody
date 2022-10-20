package com.woowacourse.smody.push.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.dto.UnSubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushSubscriptionApiService {

    private final PushSubscriptionService pushSubscriptionService;
    private final MemberService memberService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void subscribe(TokenPayload tokenPayload, SubscriptionRequest subscriptionRequest) {
        Member member = memberService.search(tokenPayload.getId());
        PushSubscription subscription = pushSubscriptionService.findByEndpoint(subscriptionRequest.endpoint)
                .map(pushSubscription -> getUpdatedSubscription(member, pushSubscription))
                .orElseGet(() -> pushSubscriptionService.create(subscriptionRequest.toPushSubscriptionEntity(member)));

        applicationEventPublisher.publishEvent(new PushSubscribeEvent(subscription));
    }

    private PushSubscription getUpdatedSubscription(final Member member, final PushSubscription pushSubscription) {
        pushSubscription.updateMember(member);
        return pushSubscription;
    }

    @Transactional
    public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
        memberService.search(tokenPayload.getId());
        pushSubscriptionService.deleteByEndpoint(unSubscription.getEndpoint());
    }
}
