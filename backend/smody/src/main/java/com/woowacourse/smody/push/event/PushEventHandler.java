package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushEventHandler implements ApplicationListener<PushEvent> {

	private final WebPushService webPushService;
	private final PushNotificationRepository pushNotificationRepository;

	@Override
	public void onApplicationEvent(PushEvent event) {
		PushMapper.from(event.getSource())
			.send(
				event.getEntity(),
				pushNotificationRepository,
				webPushService
			);
	}
}
