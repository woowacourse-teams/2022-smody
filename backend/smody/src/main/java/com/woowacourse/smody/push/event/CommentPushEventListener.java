package com.woowacourse.smody.push.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.comment.service.CommentService;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.service.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentPushEventListener {

    private final PushNotificationService pushNotificationService;
    private final CommentService commentService;

    @Transactional
    @Async("asyncExecutor")
    @TransactionalEventListener
    public void handle(CommentCreateEvent event) {
        Comment comment = commentService.search(event.getComment().getId());
        Member cycleDetailWriter = extractDetailWriter(comment);

        if (isSelfCommented(comment, cycleDetailWriter)) {
            return;
        }

        pushNotificationService.register(buildNotification(comment));
    }

    private Member extractDetailWriter(Comment comment) {
        return comment.getCycleDetail().getCycle().getMember();
    }

    private boolean isSelfCommented(Comment comment, Member cycleDetailWriter) {
        return cycleDetailWriter.matchId(comment.getMember().getId());
    }

    public PushNotification buildNotification(Object entity) {
        Comment comment = (Comment) entity;
        Member cycleDetailWriter = extractDetailWriter(comment);
        Member commentWriter = comment.getMember();
        return PushNotification.builder()
                .message(commentWriter.getNickname() + "님께서 회원님의 피드에 댓글을 남겼어요!")
                .pushTime(comment.getCreatedAt())
                .pushStatus(PushStatus.IN_COMPLETE)
                .pushCase(PushCase.COMMENT)
                .member(cycleDetailWriter)
                .pathId(comment.getCycleDetail().getId())
                .build();
    }
}
