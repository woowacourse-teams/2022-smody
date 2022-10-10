package com.woowacourse.smody.push.event;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.service.PushNotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChallengePushEventListener {

    private final PushNotificationService pushNotificationService;
    private final CycleService cycleService;

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(CycleCreateEvent event) {
        executePush(event.getCycle());
    }

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(CycleProgressEvent event) {
        executePush(event.getCycle());
    }

    private void executePush(Cycle cycle) {
        cycle = cycleService.search(cycle.getId());
        deleteInCompleteNotificationIfSamePathIdPresent(cycle);
        if (cycle.isSuccess()) {
            return;
        }
        pushNotificationService.register(buildNotification(cycle));
    }

    private void deleteInCompleteNotificationIfSamePathIdPresent(Cycle cycle) {
        pushNotificationService.searchSamePathAndStatus(cycle.getId(), PushStatus.IN_COMPLETE)
                .ifPresent(notification -> pushNotificationService.delete(notification.getId()));
    }

    public PushNotification buildNotification(Object entity) {
        Cycle cycle = (Cycle) entity;
        Challenge challenge = cycle.getChallenge();
        LocalDateTime pushTime = extractPushTime(cycle);
        return PushNotification.builder()
                .message(challenge.getName() + " 인증까지 얼마 안남았어요~")
                .pushTime(pushTime)
                .pushStatus(PushStatus.IN_COMPLETE)
                .member(cycle.getMember())
                .pushCase(PushCase.CHALLENGE)
                .pathId(cycle.getId())
                .build();
    }

    private LocalDateTime extractPushTime(Cycle cycle) {
        return cycle.getStartTime()
                .plusDays(cycle.getInterval())
                .minusHours(3L);
    }
}
