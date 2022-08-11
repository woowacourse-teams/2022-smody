package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationEvent;

import com.woowacourse.smody.domain.PushSubscription;

import lombok.Getter;

@Getter
public class SubscriptionPushEvent extends ApplicationEvent {

	private final PushSubscription pushSubscription;

	public SubscriptionPushEvent(Object source, PushSubscription pushSubscription) {
		super(source);
		this.pushSubscription = pushSubscription;
	}
}
