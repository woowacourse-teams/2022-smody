package com.woowacourse.smody.push.event;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.repository.PushNotificationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CycleProgressPushHandler implements ApplicationListener<CycleProgressPushEvent> {

	private final PushNotificationRepository pushNotificationRepository;

	@Override
	@Transactional
	public void onApplicationEvent(CycleProgressPushEvent event) {
		Cycle cycle = event.getCycle();
		if (cycle.isSuccess()) {
			return;
		}
		LocalDateTime pushTime = cycle.getStartTime()
			.plusDays(cycle.getInterval())
			.minusHours(3L);

		Challenge challenge = cycle.getChallenge();
		PushNotification pushNotification = new PushNotification(
			challenge.getName() + " 인증까지 얼마 안남았어요~",
			pushTime,
			PushStatus.IN_COMPLETE,
			cycle.getMember()
		);
		pushNotificationRepository.save(pushNotification);
	}
}
