package com.woowacourse.smody.push.event;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushContent;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CycleProgressPushHandler implements ApplicationListener<CycleProgressPushEvent> {

	private final PushSubscriptionService pushSubscriptionService;
	private final WebPushService webPushService;
	private final TaskScheduler taskScheduler;

	@Override
	public void onApplicationEvent(CycleProgressPushEvent event) {
		Cycle cycle = event.getCycle();

		long pushTime = cycle.getStartTime()
			.plusDays(cycle.getInterval())
			.minusHours(3L)
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.toEpochMilli();

		List<PushSubscription> subscriptions = pushSubscriptionService.searchByMember(cycle.getMember());

		taskScheduler.schedule(
			() -> checkSendPushOrNot(cycle, subscriptions),
			triggerContext -> new Date(pushTime)
		);
	}

	private void checkSendPushOrNot(Cycle cycle, List<PushSubscription> subscriptions) {
		if (cycle.isIncreasePossible(LocalDateTime.now())) {
			sendNotifications(cycle.getChallenge(), subscriptions);
		}
	}

	private void sendNotifications(Challenge challenge, List<PushSubscription> subscriptions) {
		for (PushSubscription subscription : subscriptions) {
			webPushService.sendNotification(subscription, PushContent.builder()
				.message(challenge.getName() + " 인증까지 얼마 안남았어요~")
				.build()
			);
		}
	}
}
