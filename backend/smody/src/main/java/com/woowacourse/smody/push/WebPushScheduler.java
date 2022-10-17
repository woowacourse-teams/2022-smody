package com.woowacourse.smody.push;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebPushScheduler {

    private final WebPushBatch webPushBatch;

    @Scheduled(initialDelay = 5000, fixedDelayString = "${push.schedule.period}")
    public void execute() {
        webPushBatch.sendPushNotifications();
    }
}
