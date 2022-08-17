package com.woowacourse.smody.push.strategy;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.service.PushNotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChallengePushStrategy implements PushStrategy {

	private final PushNotificationService pushNotificationService;

	@Override
	@Transactional
	public void push(Object entity) {
		Cycle cycle = (Cycle)entity;
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

	@Override
	public PushNotification buildNotification(Object entity) {
		Cycle cycle = (Cycle)entity;
		Challenge challenge = cycle.getChallenge();
		LocalDateTime pushTime = extractPushTime(cycle);
		return PushNotification.builder()
			.message(challenge.getName() + " 인증까지 얼마 안남았어요~")
			.pushTime(pushTime)
			.pushStatus(PushStatus.IN_COMPLETE)
			.member(cycle.getMember())
			.pushCase(getPushCase())
			.pathId(cycle.getId())
			.build();
	}

	private LocalDateTime extractPushTime(Cycle cycle) {
		return cycle.getStartTime()
			.plusDays(cycle.getInterval())
			.minusHours(3L);
	}

	@Override
	public PushCase getPushCase() {
		return PushCase.CHALLENGE;
	}
}
