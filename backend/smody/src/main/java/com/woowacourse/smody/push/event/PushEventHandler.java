package com.woowacourse.smody.push.event;

import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushEventHandler implements ApplicationListener<PushEvent> {

    private final WebPushService webPushService;
    private final PushNotificationRepository pushNotificationRepository;

    @Override
    public void onApplicationEvent(PushEvent event) {
        PushCase eventCase = event.getEventCase();
        eventCase.push(
                event.getEntity(),
                pushNotificationRepository,
                webPushService
        );
    }
}
