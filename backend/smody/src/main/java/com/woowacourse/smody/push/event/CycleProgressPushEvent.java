package com.woowacourse.smody.push.event;

import org.springframework.context.ApplicationEvent;

import com.woowacourse.smody.domain.Cycle;

import lombok.Getter;

@Getter
public class CycleProgressPushEvent extends ApplicationEvent {

	private final Cycle cycle;

	public CycleProgressPushEvent(Object source, Cycle cycle) {
		super(source);
		this.cycle = cycle;
	}
}
