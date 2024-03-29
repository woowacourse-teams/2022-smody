package com.woowacourse.smody.push.dto;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushSubscription;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

@NoArgsConstructor
public class SubscriptionRequest extends Subscription {

    public SubscriptionRequest(String endpoint, String p256dh, String auth) {
        super(endpoint, new Keys(p256dh, auth));
    }

    public PushSubscription toPushSubscriptionEntity(Member member) {
        return new PushSubscription(endpoint, keys.p256dh, keys.auth, member);
    }
}
