package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.event.PushEvent;
import com.woowacourse.smody.repository.PushSubscriptionRepository;
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
        Member member = memberService.search(tokenPayload);
        PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
                .map(pushSubscription -> pushSubscription.updateMember(member))
                .orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

		applicationEventPublisher.publishEvent(new PushEvent(subscription, PushCase.SUBSCRIPTION));
	}

    @Transactional
    public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
        memberService.search(tokenPayload);
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
