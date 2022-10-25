package com.woowacourse.smody.push;

import static java.util.stream.Collectors.groupingBy;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.api.WebPushApi;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.service.PushNotificationService;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WebPushBatch {

    private final PushNotificationService pushNotificationService;
    private final PushSubscriptionService pushSubscriptionService;
    private final WebPushApi webPushApi;

    @Transactional
    public void sendPushNotifications() {
        List<PushNotification> pushableNotifications = pushNotificationService.searchPushable();

        Map<Member, List<PushNotification>> notificationsByMember = groupByMemberNotifications(pushableNotifications);

        List<Member> members = new ArrayList<>(notificationsByMember.keySet());
        Map<Member, List<PushSubscription>> subscriptionsByMember = groupByMemberSubscriptions(members);

        for (Member member : notificationsByMember.keySet()) {
            sendNotificationsToMember(pushableNotifications, notificationsByMember, subscriptionsByMember, member);
        }

        pushNotificationService.completeAll(pushableNotifications);
    }

    private Map<Member, List<PushNotification>> groupByMemberNotifications(List<PushNotification> notifications) {
        return notifications.stream()
                .collect(groupingBy(PushNotification::getMember));
    }

    private Map<Member, List<PushSubscription>> groupByMemberSubscriptions(List<Member> members) {
        return pushSubscriptionService.findByMembers(members)
                .stream()
                .collect(groupingBy(PushSubscription::getMember));
    }

    private void sendNotificationsToMember(List<PushNotification> pushableNotifications,
                                           Map<Member, List<PushNotification>> notificationsByMember,
                                           Map<Member, List<PushSubscription>> subscriptionsByMember,
                                           Member member) {
        for (PushNotification notification : notificationsByMember.get(member)) {
            List<PushSubscription> subscriptions = subscriptionsByMember.getOrDefault(member, List.of());
            sendNotificationsToSubscriptions(pushableNotifications, notification, subscriptions);
        }
    }

    private void sendNotificationsToSubscriptions(List<PushNotification> pushableNotifications,
                                                  PushNotification notification,
                                                  List<PushSubscription> subscriptions) {
        for (PushSubscription subscription : subscriptions) {
            boolean isValidSubscription = webPushApi.sendNotification(subscription, notification);
            handleFailedPush(pushableNotifications, notification, subscription, isValidSubscription);
        }
    }

    private void handleFailedPush(List<PushNotification> pushableNotifications,
                                  PushNotification notification,
                                  PushSubscription subscription,
                                  boolean isValidSubscription) {
        if (!isValidSubscription) {
            pushSubscriptionService.delete(subscription);
            pushableNotifications.remove(notification);
        }
    }
}
