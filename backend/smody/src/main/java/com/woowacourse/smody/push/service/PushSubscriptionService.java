package com.woowacourse.smody.push.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final MemberService memberService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void subscribe(TokenPayload tokenPayload, SubscriptionRequest subscriptionRequest) {
        Member member = memberService.search(tokenPayload.getId());
        PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
                .map(pushSubscription -> pushSubscription.updateMember(member))
                .orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

        applicationEventPublisher.publishEvent(new PushSubscribeEvent(subscription));
    }

    @Transactional
    public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
        memberService.search(tokenPayload.getId());
        pushSubscriptionRepository.deleteByEndpoint(unSubscription.getEndpoint());
    }

    public List<PushSubscription> searchByMembers(List<Member> members) {
        return pushSubscriptionRepository.findByMemberIn(members);
    }

    @Transactional
    public void delete(PushSubscription pushSubscription) {
        pushSubscriptionRepository.delete(pushSubscription);
    }
}
