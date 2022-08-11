package com.woowacourse.smody.push;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushSchedulerExecutor {

	private final PushScheduler pushScheduler;

	@Scheduled(initialDelay = 5000, fixedDelayString = "${push.schedule.period}")
	public void execute() {
		pushScheduler.sendPushNotifications();
	}
}
