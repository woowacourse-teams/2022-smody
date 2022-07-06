package com.woowacourse.smody.domain;

import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "challenge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    private LocalDateTime startTime;

    @Builder
    public Cycle(Member member, Challenge challenge, Progress progress, LocalDateTime startTime) {
        if (startTime.isAfter(LocalDateTime.now())) {
            throw new BusinessException(ExceptionData.INVALID_START_TIME);
        }
        this.member = member;
        this.challenge = challenge;
        this.progress = progress;
        this.startTime = startTime;
    }

    public void increaseProgress(LocalDateTime progressTime) {
        progress = progress.increase(startTime, progressTime);
    }

    public boolean matchMember(Long memberId) {
        return member.matchId(memberId);
    }

    public boolean isInProgress(LocalDateTime now) {
        return progress.isInProgress(startTime, now);
    }
}
