package com.woowacourse.smody.domain;

import java.time.LocalDateTime;

import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.WebPushService;

public enum PushCase {

	SUBSCRIPTION {
		@Override
		public void push(Object entity, PushNotificationRepository repository, WebPushService webPushService) {
			PushSubscription pushSubscription = (PushSubscription)entity;

			Member member = pushSubscription.getMember();
			PushNotification pushNotification = repository.save(PushNotification.builder()
				.message(member.getNickname() + "님 스모디 알림이 구독되었습니다.")
				.pushTime(LocalDateTime.now())
				.pushStatus(PushStatus.COMPLETE)
				.member(member)
				.pushCase(PushCase.SUBSCRIPTION)
				.build()
			);

			webPushService.sendNotification(pushSubscription, pushNotification);
		}
	},

	CHALLENGE {
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
			repository.save(PushNotification.builder()
				.message(challenge.getName() + " 인증까지 얼마 안남았어요~")
				.pushTime(pushTime)
				.pushStatus(PushStatus.IN_COMPLETE)
				.member(cycle.getMember())
				.pushCase(PushCase.CHALLENGE)
				.pathId(cycle.getId())
				.build());
		}
	};

	public abstract void push(
		Object entity,
		PushNotificationRepository repository,
		WebPushService webPushService
	);
}
