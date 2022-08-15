package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

@NoArgsConstructor
public class SubscriptionRequest extends Subscription {

    public SubscriptionRequest(String endpoint, String p256dh, String auth) {
        super(endpoint, new Keys(p256dh, auth));
    }

    public PushSubscription toEntity(Member member) {
        return new PushSubscription(endpoint, keys.p256dh, keys.auth, member);
    }
}
