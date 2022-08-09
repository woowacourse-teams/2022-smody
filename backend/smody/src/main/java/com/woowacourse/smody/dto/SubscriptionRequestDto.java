package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;

import nl.martijndwars.webpush.Subscription;

public class SubscriptionRequestDto extends Subscription {

	public PushSubscription toEntity(Member member) {
		return new PushSubscription(endpoint, keys.p256dh, keys.auth, member);
	}
}
