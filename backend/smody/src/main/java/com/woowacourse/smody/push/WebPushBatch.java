package com.woowacourse.smody.push;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.service.PushNotificationService;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.push.service.WebPushService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebPushBatch {

    private final PushNotificationService pushNotificationService;
    private final PushSubscriptionService pushSubscriptionService;
    private final WebPushService webPushService;

    @Transactional
    public void sendPushNotifications() {
        List<PushNotification> notifications = pushNotificationService.searchPushable();

        Map<Member, List<PushNotification>> notificationsByMember = groupByMemberNotifications(notifications);

        List<Member> members = new ArrayList<>(notificationsByMember.keySet());
        Map<Member, List<PushSubscription>> subscriptionsByMember = groupByMemberSubscriptions(members);

        for (Member member : notificationsByMember.keySet()) {
            sendNotificationsToMember(notifications, notificationsByMember, subscriptionsByMember, member);
        }

        pushNotificationService.completeAll(notifications);
    }

    private void sendNotificationsToMember(List<PushNotification> notifications,
        Map<Member, List<PushNotification>> notificationsByMember,
        Map<Member, List<PushSubscription>> subscriptionsByMember,
        Member member) {
        for (PushNotification notification : notificationsByMember.get(member)) {
            List<PushSubscription> subscriptions = subscriptionsByMember.getOrDefault(member, List.of());
            sendNotificationsToSubscriptions(notifications, notification, subscriptions);
        }
    }

    private void sendNotificationsToSubscriptions(List<PushNotification> notifications, PushNotification notification,
        List<PushSubscription> subscriptions) {
        for (PushSubscription pushSubscription : subscriptions) {
            boolean isValidSubscription = webPushService.sendNotification(pushSubscription, notification);
            handleFailedPush(notifications, notification, pushSubscription, isValidSubscription);
        }
    }

    private void handleFailedPush(List<PushNotification> notifications, PushNotification notification,
        PushSubscription pushSubscription, boolean isValidSubscription) {
        if (!isValidSubscription) {
            pushSubscriptionService.delete(pushSubscription);
            notifications.remove(notification);
        }
    }

    private Map<Member, List<PushNotification>> groupByMemberNotifications(List<PushNotification> notifications) {
        return notifications.stream()
                .collect(groupingBy(PushNotification::getMember));
    }

    private Map<Member, List<PushSubscription>> groupByMemberSubscriptions(List<Member> members) {
        return pushSubscriptionService.searchByMembers(members)
                .stream()
                .collect(groupingBy(PushSubscription::getMember));
    }
}
