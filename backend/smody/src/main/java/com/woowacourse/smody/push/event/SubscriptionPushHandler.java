package com.woowacourse.smody.push.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionPushHandler implements ApplicationListener<SubscriptionPushEvent> {

	private final WebPushService webPushService;
	private final PushNotificationRepository pushNotificationRepository;

	@Override
	@Transactional
	public void onApplicationEvent(SubscriptionPushEvent event) {
		PushSubscription pushSubscription = event.getPushSubscription();
		PushNotification pushNotification = pushNotificationRepository.save(new PushNotification(
			pushSubscription.getMember().getNickname() + "님 스모디 알림이 구독되었습니다.",
			LocalDateTime.now(),
			PushStatus.COMPLETE,
			pushSubscription.getMember()
		));

		webPushService.sendNotification(pushSubscription, pushNotification);
	}
}
