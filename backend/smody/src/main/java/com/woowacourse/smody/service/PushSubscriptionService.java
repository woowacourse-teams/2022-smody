package com.woowacourse.smody.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.event.SubscriptionPushEvent;
import com.woowacourse.smody.push.event.SubscriptionPushHandler;
import com.woowacourse.smody.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushSubscriptionService {

	private final PushSubscriptionRepository pushSubscriptionRepository;
	private final MemberService memberService;
	private final SubscriptionPushHandler subscriptionPushHandler;

	@Transactional
	public void subscribe(TokenPayload tokenPayload, SubscriptionRequest subscriptionRequest) {
		Member member = memberService.search(tokenPayload);
		PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
			.map(pushSubscription -> pushSubscription.updateMember(member))
			.orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

		subscriptionPushHandler.onApplicationEvent(new SubscriptionPushEvent(this, subscription));
	}

	@Transactional
	public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
		memberService.search(tokenPayload);
		pushSubscriptionRepository.deleteByEndpoint(unSubscription.getEndpoint());
	}

	public List<PushSubscription> searchByMember(Member member) {
		return pushSubscriptionRepository.findByMember(member);
	}
}
