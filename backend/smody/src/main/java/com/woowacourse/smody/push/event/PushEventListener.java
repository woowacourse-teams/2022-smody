package com.woowacourse.smody.push.event;

import static java.util.stream.Collectors.toMap;

import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.strategy.PushStrategy;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PushEventListener {

    private final Map<PushCase, PushStrategy> pushStrategies;

    public PushEventListener(List<PushStrategy> pushStrategies) {
        this.pushStrategies = pushStrategies.stream()
                .collect(toMap(
                        PushStrategy::getPushCase,
                        pushStrategy -> pushStrategy)
                );
    }

    @Async
    @TransactionalEventListener
    public void handlePushSubscribe(PushSubscribeEvent event) {
        pushStrategies.get(PushCase.SUBSCRIPTION)
            .push(event.getPushSubscription());
    }

    @Async
    @TransactionalEventListener
    public void handleCycleCreate(CycleCreateEvent event) {
        pushStrategies.get(PushCase.CHALLENGE)
            .push(event.getCycle());
    }

    @Async
    @TransactionalEventListener
    public void handleCycleProgress(CycleProgressEvent event) {
        pushStrategies.get(PushCase.CHALLENGE)
            .push(event.getCycle());
    }

    @Async
    @TransactionalEventListener
    public void handleCommentCreate(CommentCreateEvent event) {
        pushStrategies.get(PushCase.COMMENT)
            .push(event.getComment());
    }
}
