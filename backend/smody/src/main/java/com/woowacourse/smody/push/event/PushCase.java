package com.woowacourse.smody.push.event;

import java.time.LocalDateTime;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.WebPushService;

public enum PushCase {

	SUBSCRIPTION {
		@Override
		public void push(Object entity, PushNotificationRepository repository, WebPushService webPushService) {
			PushSubscription pushSubscription = (PushSubscription) entity;
			PushNotification pushNotification = repository.save(new PushNotification(
				pushSubscription.getMember().getNickname() + "님 스모디 알림이 구독되었습니다.",
				LocalDateTime.now(),
				PushStatus.COMPLETE,
				pushSubscription.getMember()
			));

			webPushService.sendNotification(pushSubscription, pushNotification);
		}
	},

	PROGRESS {
		@Override
		public void push(Object entity, PushNotificationRepository repository, WebPushService webPushService) {
			Cycle cycle = (Cycle) entity;
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
			repository.save(pushNotification);
		}
	};

	public abstract void push(
		Object entity,
		PushNotificationRepository repository,
		WebPushService webPushService
	);
}
