package com.woowacourse.smody.push.event;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.service.PushNotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SubscriptionPushEventListener {

    private final PushNotificationService pushNotificationService;

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(PushSubscribeEvent event) {
        PushSubscription pushSubscription = event.getPushSubscription();
        Member member = pushSubscription.getMember();
        pushNotificationService.register(buildNotification(member));
    }

    public PushNotification buildNotification(Object entity) {
        Member member = (Member) entity;
        return PushNotification.builder()
                .message(member.getNickname() + "님 스모디 알림이 구독되었습니다.")
                .pushTime(LocalDateTime.now())
                .pushStatus(PushStatus.IN_COMPLETE)
                .member(member)
                .pushCase(PushCase.SUBSCRIPTION)
                .build();
    }
}
