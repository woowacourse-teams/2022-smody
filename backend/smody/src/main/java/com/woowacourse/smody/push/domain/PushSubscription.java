package com.woowacourse.smody.push.domain;

import com.woowacourse.smody.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_column_in_push_subscription", columnNames = "endpoint")}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PushSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_subscription_id")
    private Long id;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String p256dh;

    @Column(nullable = false)
    private String auth;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public PushSubscription(String endpoint, String p256dh, String auth, Member member) {
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
        this.member = member;
    }

    public void updateMember(Member member) {
        this.member = member;
    }
}
