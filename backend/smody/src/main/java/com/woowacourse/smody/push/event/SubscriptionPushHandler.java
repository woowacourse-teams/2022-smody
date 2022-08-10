package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.woowacourse.smody.domain.PushContent;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionPushHandler implements ApplicationListener<SubscriptionPushEvent> {

	private final WebPushService webPushService;

	@Override
	public void onApplicationEvent(SubscriptionPushEvent event) {
		PushSubscription pushSubscription = event.getPushSubscription();
		webPushService.sendNotification(pushSubscription, PushContent.builder()
			.message(pushSubscription.getMember().getNickname() + "님 스모디 알림이 구독되었습니다.")
			.build());
	}
}
