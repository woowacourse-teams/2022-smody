package com.woowacourse.smody.push.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.CycleService;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushEventHandler implements ApplicationListener<PushEvent> {

	private final WebPushService webPushService;
	private final PushNotificationRepository pushNotificationRepository;

	@Override
	public void onApplicationEvent(PushEvent event) {
		Object source = event.getSource();
		handleSubscriptionEvent(event, source);
		handleProgressEvent(event, source);
	}

	private void handleSubscriptionEvent(PushEvent event, Object source) {
		if (source instanceof PushSubscriptionService) {
			PushSubscription pushSubscription = (PushSubscription) event.getEntity();
			PushNotification pushNotification = pushNotificationRepository.save(new PushNotification(
				pushSubscription.getMember().getNickname() + "님 스모디 알림이 구독되었습니다.",
				LocalDateTime.now(),
				PushStatus.COMPLETE,
				pushSubscription.getMember()
			));

			webPushService.sendNotification(pushSubscription, pushNotification);
		}
	}

	private void handleProgressEvent(PushEvent event, Object source) {
		if (source instanceof CycleService) {
			Cycle cycle = (Cycle) event.getEntity();
			if (cycle.isSuccess()) {
				return;
			}
			LocalDateTime pushTime = cycle.getStartTime()
				.plusDays(cycle.getInterval())
				.minusHours(3L);

			Challenge challenge = cycle.getChallenge();
			PushNotification pushNotification = new PushNotification(
				challenge.getName() + " 인증까지 얼마 안남았어요~",
				pushTime,
				PushStatus.IN_COMPLETE,
				cycle.getMember()
			);
			pushNotificationRepository.save(pushNotification);
		}
	}
}
