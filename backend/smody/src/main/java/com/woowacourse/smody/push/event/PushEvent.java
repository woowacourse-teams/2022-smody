package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationEvent;

import com.woowacourse.smody.domain.PushCase;

import lombok.Getter;

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
