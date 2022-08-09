package com.woowacourse.smody.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushContent;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushSubscriptionService {

	private final PushSubscriptionRepository pushSubscriptionRepository;
	private final MemberService memberService;
	private final WebPushService webPushService;

	public String getPublicKey() {
		return webPushService.getPublicKey();
	}

	@Transactional
	public void subscribe(TokenPayload tokenPayload, SubscriptionRequest subscriptionRequest) {
		Member member = memberService.search(tokenPayload);
		PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
			.map(pushSubscription -> pushSubscription.updateMember(member))
			.orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

		webPushService.sendNotification(subscription, PushContent.builder()
			.title("구독 완료")
			.message(member.getNickname() + "님 스모디 알림이 구독되었습니다.")
			.build());
	}

	@Transactional
	public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequest unSubscription) {
		memberService.search(tokenPayload);
		pushSubscriptionRepository.deleteByEndpoint(unSubscription.getEndpoint());
	}
}
