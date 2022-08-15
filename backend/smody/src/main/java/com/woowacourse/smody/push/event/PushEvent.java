package com.woowacourse.smody.push.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PushEvent extends ApplicationEvent {

    private final Object entity;
    private final PushCase eventCase;

    public PushEvent(Object source, Object entity, PushCase eventCase) {
        super(source);
        this.entity = entity;
        this.eventCase = eventCase;
    }
}
