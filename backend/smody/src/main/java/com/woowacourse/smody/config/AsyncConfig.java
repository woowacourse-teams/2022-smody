package com.woowacourse.smody.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Value("${async.threads.max}")
	private Integer threadMax;

	@Bean
	public TaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
		asyncExecutor.setThreadNamePrefix("async-pool");
		asyncExecutor.setCorePoolSize(threadMax);
		asyncExecutor.initialize();
		return asyncExecutor;
	}
}
