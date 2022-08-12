package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.event.PushEvent;
import com.woowacourse.smody.push.event.PushEventHandler;
import com.woowacourse.smody.repository.PushSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PushSubscriptionService {

	private final PushSubscriptionRepository pushSubscriptionRepository;
	private final MemberService memberService;
	private final PushEventHandler pushEventHandler;

	@Transactional
	public void subscribe(TokenPayload tokenPayload, SubscriptionRequest subscriptionRequest) {
		Member member = memberService.search(tokenPayload);
		PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
			.map(pushSubscription -> pushSubscription.updateMember(member))
			.orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

		pushEventHandler.onApplicationEvent(new PushEvent(this, subscription));
	}

	@Transactional
	public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
		memberService.search(tokenPayload);
		pushSubscriptionRepository.deleteByEndpoint(unSubscription.getEndpoint());
	}

	public List<PushSubscription> searchByMembers(List<Member> members) {
		return pushSubscriptionRepository.findByMemberIn(members);
	}

	public void delete(PushSubscription pushSubscription) {
		pushSubscriptionRepository.delete(pushSubscription);
	}
}
