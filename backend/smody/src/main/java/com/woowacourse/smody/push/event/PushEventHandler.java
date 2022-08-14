package com.woowacourse.smody.push.event;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.push.strategy.PushStrategy;

@Component
public class PushEventHandler implements ApplicationListener<PushEvent> {

	private final Map<PushCase, PushStrategy> pushStrategies;

	public PushEventHandler(List<PushStrategy> pushStrategies) {
		this.pushStrategies = pushStrategies.stream()
			.collect(toMap(
				PushStrategy::getPushCase,
				pushStrategy -> pushStrategy)
			);
	}

	@Override
	public void onApplicationEvent(PushEvent event) {
		pushStrategies.get(event.getPushCase())
			.push(event.getEntity());
	}
}
