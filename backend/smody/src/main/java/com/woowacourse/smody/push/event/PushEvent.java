package com.woowacourse.smody.push.event;

import com.woowacourse.smody.domain.PushCase;
import lombok.Getter;

@Getter
public class PushEvent {

	private final Object entity;
	private final PushCase pushCase;

	public PushEvent(Object entity, PushCase pushCase) {
		this.entity = entity;
		this.pushCase = pushCase;
	}
}
