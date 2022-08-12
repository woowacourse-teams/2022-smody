package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class PushEvent extends ApplicationEvent {

	private final Object entity;

	public PushEvent(Object source, Object entity) {
		super(source);
		this.entity = entity;
	}
}
