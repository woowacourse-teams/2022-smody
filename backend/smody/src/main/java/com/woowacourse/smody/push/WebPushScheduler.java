package com.woowacourse.smody.push;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class WebPushScheduler {

    private final WebPushBatch webPushBatch;

    @Scheduled(initialDelay = 5000, fixedDelayString = "${push.schedule.period}")
    public void execute() {
        webPushBatch.sendPushNotifications();
    }
}
