package com.woowacourse.smody.push.strategy;

import com.woowacourse.smody.util.TimeStrategy;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.service.PushNotificationService;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionPushStrategy implements PushStrategy {

	private final PushNotificationService pushNotificationService;
	private final WebPushService webPushService;
	private final TimeStrategy timeStrategy;

	@Override
	@Transactional
	public void push(Object entity) {
		PushSubscription pushSubscription = (PushSubscription)entity;

		Member member = pushSubscription.getMember();
		PushNotification pushNotification = pushNotificationService.register(buildNotification(member));

		webPushService.sendNotification(pushSubscription, pushNotification);
	}

	@Override
	public PushNotification buildNotification(Object entity) {
		Member member = (Member)entity;
		return PushNotification.builder()
			.message(member.getNickname() + "님 스모디 알림이 구독되었습니다.")
			.pushTime(timeStrategy.now())
			.pushStatus(PushStatus.COMPLETE)
			.member(member)
			.pushCase(getPushCase())
			.build();
	}

	@Override
	public PushCase getPushCase() {
		return PushCase.SUBSCRIPTION;
	}
}
