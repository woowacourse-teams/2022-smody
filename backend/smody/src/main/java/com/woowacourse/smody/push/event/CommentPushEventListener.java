package com.woowacourse.smody.push.event;

import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.comment.service.CommentService;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.service.PushNotificationService;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.push.service.WebPushService;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommentPushEventListener {

    private final PushNotificationService pushNotificationService;
    private final PushSubscriptionService pushSubscriptionService;
    private final CommentService commentService;
    private final WebPushService webPushService;

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(CommentCreateEvent event) {
        Comment comment = commentService.search(event.getComment().getId());
        Member cycleDetailWriter = extractDetailWriter(comment);

        if (cycleDetailWriter.matchId(comment.getMember().getId())) {
            return;
        }

        PushNotification pushNotification = pushNotificationService.register(buildNotification(comment));

        List<PushSubscription> subscriptions = pushSubscriptionService.searchByMembers(List.of(cycleDetailWriter));
        for (PushSubscription subscription : subscriptions) {
            webPushService.sendNotification(subscription, pushNotification);
        }
    }

    public PushNotification buildNotification(Object entity) {
        Comment comment = (Comment) entity;
        Member cycleDetailWriter = extractDetailWriter(comment);
        Member commentWriter = comment.getMember();
        return PushNotification.builder()
                .message(commentWriter.getNickname() + "님께서 회원님의 피드에 댓글을 남겼어요!")
                .pushTime(comment.getCreatedAt())
                .pushStatus(PushStatus.COMPLETE)
                .pushCase(PushCase.COMMENT)
                .member(cycleDetailWriter)
                .pathId(comment.getCycleDetail().getId())
                .build();
    }

    private Member extractDetailWriter(Comment comment) {
        return comment.getCycleDetail().getCycle().getMember();
    }
}
