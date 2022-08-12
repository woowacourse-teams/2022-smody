package com.woowacourse.smody.push.event;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.CycleService;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

public enum PushMapper {

	SUBSCRIPTION(PushSubscriptionService.class) {
		@Override
		public void send(Object entity, PushNotificationRepository repository, WebPushService webPushService) {
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

	PROGRESS(CycleService.class) {
		@Override
		public void send(Object entity, PushNotificationRepository repository, WebPushService webPushService) {
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

	private final Object source;

	PushMapper(Object source) {
		this.source = source;
	}

	public static PushMapper from(Object source) {
		return Arrays.stream(values())
			.filter(mapper -> source.getClass().equals(mapper.source))
			.findAny()
			.orElseThrow();
	}

	public abstract void send(
		Object entity,
		PushNotificationRepository repository,
		WebPushService webPushService
	);
}
