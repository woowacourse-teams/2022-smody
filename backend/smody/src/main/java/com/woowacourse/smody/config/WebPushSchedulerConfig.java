package com.woowacourse.smody.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.woowacourse.smody.push.WebPushBatch;
import com.woowacourse.smody.push.WebPushScheduler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WebPushSchedulerConfig {
    
    private final WebPushBatch webPushBatch;
    
    @Bean
    @ConditionalOnProperty(value = "batch.schedule.enabled", havingValue = "true")
    public WebPushScheduler webPushScheduler() {
        return new WebPushScheduler(webPushBatch);
    }
}