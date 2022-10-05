package com.woowacourse.smody.push.domain;

import com.woowacourse.smody.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PushNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_notification_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime pushTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PushStatus pushStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PushCase pushCase;

    private Long pathId;

    @Builder
    public PushNotification(String message, LocalDateTime pushTime, PushStatus pushStatus, Member member,
                            PushCase pushCase, Long pathId) {
        this.message = message;
        this.pushTime = pushTime;
        this.pushStatus = pushStatus;
        this.member = member;
        this.pushCase = pushCase;
        this.pathId = pathId;
    }

    public boolean isPushable(LocalDateTime now) {
        return pushStatus == PushStatus.IN_COMPLETE
                && (pushTime.isBefore(now) || pushTime.isEqual(now));
    }

    public void completePush() {
        this.pushStatus = PushStatus.COMPLETE;
    }
}
