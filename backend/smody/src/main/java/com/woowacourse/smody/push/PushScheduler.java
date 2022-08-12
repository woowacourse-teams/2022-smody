package com.woowacourse.smody.push;

import static java.util.stream.Collectors.groupingBy;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PushScheduler {

	private final PushNotificationRepository pushNotificationRepository;
	private final PushSubscriptionService pushSubscriptionService;
	private final WebPushService webPushService;

	@Transactional
	public void sendPushNotifications() {
		List<PushNotification> notifications = pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE);

		LocalDateTime now = LocalDateTime.now();
		Map<Member, List<PushNotification>> notificationsByMember = groupByMemberNotifications(notifications, now);

		List<Member> members = new ArrayList<>(notificationsByMember.keySet());
		Map<Member, List<PushSubscription>> subscriptionsByMember = groupByMemberSubscriptions(members);

		for (Member member : notificationsByMember.keySet()) {
			sendAllNotificationsOfMember(notificationsByMember, subscriptionsByMember, member);
		}
	}

	private Map<Member, List<PushNotification>> groupByMemberNotifications(List<PushNotification> notifications,
		LocalDateTime now) {
		return notifications.stream()
			.filter(notification -> notification.isPushable(now))
			.collect(groupingBy(PushNotification::getMember));
	}

	private Map<Member, List<PushSubscription>> groupByMemberSubscriptions(List<Member> members) {
		return pushSubscriptionService.searchByMembers(members)
			.stream()
			.collect(groupingBy(PushSubscription::getMember));
	}

	private void sendAllNotificationsOfMember(Map<Member, List<PushNotification>> notificationsByMember,
		Map<Member, List<PushSubscription>> subscriptionsByMember,
		Member member) {
		for (PushNotification notification : notificationsByMember.get(member)) {
			sendNotification(subscriptionsByMember, member, notification);
		}
	}

	private void sendNotification(Map<Member, List<PushSubscription>> subscriptionsByMember,
		Member member,
		PushNotification notification) {
		for (PushSubscription pushSubscription : subscriptionsByMember.get(member)) {
			boolean isValidSubscription = webPushService.sendNotification(pushSubscription, notification);
			updatePushStatus(notification, isValidSubscription);
			removeInvalidSubscription(pushSubscription, isValidSubscription);
		}
	}

	private void updatePushStatus(PushNotification notification, boolean isValidSubscription) {
		if (isValidSubscription) {
			notification.completePush();
		}
	}

	private void removeInvalidSubscription(PushSubscription pushSubscription, boolean isValidSubscription) {
		if (!isValidSubscription) {
			pushSubscriptionService.delete(pushSubscription);
		}
	}
}
