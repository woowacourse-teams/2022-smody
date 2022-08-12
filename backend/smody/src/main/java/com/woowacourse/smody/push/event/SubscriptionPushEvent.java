package com.woowacourse.smody.push.event;

import com.woowacourse.smody.domain.PushSubscription;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SubscriptionPushEvent extends ApplicationEvent {

	private final PushSubscription pushSubscription;

	public SubscriptionPushEvent(Object source, PushSubscription pushSubscription) {
		super(source);
		this.pushSubscription = pushSubscription;
	}
}
