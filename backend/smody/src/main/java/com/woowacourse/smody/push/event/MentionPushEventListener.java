package com.woowacourse.smody.push.event;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.MentionCreateEvent;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.service.PushNotificationService;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.push.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MentionPushEventListener {

    private final PushNotificationService pushNotificationService;
    private final PushSubscriptionService pushSubscriptionService;
    private final MemberService memberService;

    private final WebPushService webPushService;

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(MentionCreateEvent event) {
        if (event.getMentionedIds().isEmpty() || event.getMentioningIds() == null) {
            return;
        }

        List<Member> mentionedMembers = memberService.findByIdIn(event.getMentionedIds());
        Member mentioningMember = memberService.findMember(new TokenPayload(event.getMentioningIds()));
        Long pathId = event.getCycleDetailId();

        List<PushNotification> pushNotifications = mentionedMembers.stream()
                .map(each -> pushNotificationService.register(buildNotification(mentioningMember, each, pathId)))
                .collect(Collectors.toUnmodifiableList());

        Map<Member, PushNotification> mappingNotifications = new HashMap<>();
        for (PushNotification pushNotification : pushNotifications) {
            mappingNotifications.put(pushNotification.getMember(), pushNotification);
        }

        List<PushSubscription> subscriptions = pushSubscriptionService.searchByMembers(mentionedMembers);
        for (PushSubscription subscription : subscriptions) {
            webPushService.sendNotification(subscription, mappingNotifications.get(subscription.getMember()));
        }
    }

    public PushNotification buildNotification(Member mentioning, Member mentioned, Long pathId) {
        return PushNotification.builder()
                .message(mentioning.getNickname() + "님께서 회원님을 언급하셨습니다!")
                .pushTime(LocalDateTime.now())
                .pushStatus(PushStatus.COMPLETE)
                .pushCase(PushCase.MENTION)
                .member(mentioned)
                .pathId(pathId)
                .build();
    }
}
