package com.woowacourse.smody.push.event;

import com.woowacourse.smody.domain.Cycle;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CycleProgressPushEvent extends ApplicationEvent {

	private final Cycle cycle;

	public CycleProgressPushEvent(Object source, Cycle cycle) {
		super(source);
		this.cycle = cycle;
	}
}
