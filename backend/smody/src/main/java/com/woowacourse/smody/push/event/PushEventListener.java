package com.woowacourse.smody.push.event;

import static java.util.stream.Collectors.toMap;

import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.strategy.PushStrategy;
import java.util.List;
import java.util.Map;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    public void handle(PushEvent event) {
        pushStrategies.get(event.getPushCase())
                .push(event.getEntity());
    }
}
