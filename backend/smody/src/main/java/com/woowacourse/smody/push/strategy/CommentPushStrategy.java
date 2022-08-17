package com.woowacourse.smody.push.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.service.PushNotificationService;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentPushStrategy implements PushStrategy {

	private final PushNotificationService pushNotificationService;
	private final PushSubscriptionService pushSubscriptionService;
	private final WebPushService webPushService;

	@Override
	@Transactional
	public void push(Object entity) {
		Comment comment = (Comment)entity;
		Member cycleDetailWriter = extractDetailWriter(comment);

		if (comment.getMember() == cycleDetailWriter) {
			return;
		}

		PushNotification pushNotification = pushNotificationService.register(buildNotification(entity));

		List<PushSubscription> subscriptions = pushSubscriptionService.searchByMembers(List.of(cycleDetailWriter));
		for (PushSubscription subscription : subscriptions) {
			webPushService.sendNotification(subscription, pushNotification);
		}
	}

	@Override
	public PushCase getPushCase() {
		return PushCase.COMMENT;
	}

	@Override
	public PushNotification buildNotification(Object entity) {
		Comment comment = (Comment)entity;
		Member cycleDetailWriter = extractDetailWriter(comment);
		Member commentWriter = comment.getMember();
		return PushNotification.builder()
			.message(commentWriter.getNickname() + "님께서 회원님의 피드에 댓글을 남겼어요!")
			.pushTime(comment.getCreatedAt())
			.pushStatus(PushStatus.COMPLETE)
			.pushCase(getPushCase())
			.member(cycleDetailWriter)
			.pathId(comment.getCycleDetail().getId())
			.build();
	}

	private Member extractDetailWriter(Comment comment) {
		return comment.getCycleDetail().getCycle().getMember();
	}
}
