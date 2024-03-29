package com.woowacourse.smody.push.domain;

import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
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
    public PushNotification(String message,
                            LocalDateTime pushTime,
                            PushStatus pushStatus,
                            Member member,
                            PushCase pushCase,
                            Long pathId) {
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
