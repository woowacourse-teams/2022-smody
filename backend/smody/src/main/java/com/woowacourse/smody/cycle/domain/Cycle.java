package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cycle {

    public static final long DAYS = 3L;

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

    @OneToMany(mappedBy = "cycle", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CycleDetail> cycleDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    @Column(nullable = false)
    private LocalDateTime startTime;

    public Cycle(Member member, Challenge challenge, Progress progress, LocalDateTime startTime) {
        validateStartTime(progress, startTime);
        this.member = member;
        this.challenge = challenge;
        this.progress = progress;
        this.startTime = startTime;
    }

    private void validateStartTime(Progress progress, LocalDateTime startTime) {
        if (startTime.isAfter(LocalDateTime.now().plusDays(1L))) {
            throw new BusinessException(ExceptionData.INVALID_START_TIME);
        }
        if (startTime.isAfter(LocalDateTime.now()) && progress != Progress.NOTHING) {
            throw new BusinessException(ExceptionData.INVALID_START_TIME);
        }
    }

    public void increaseProgress(LocalDateTime progressTime, Image progressImage, String description) {
        this.progress = progress.increase(startTime, progressTime);
        if (this.cycleDetails.size() <= 2) {
            this.cycleDetails.add(new CycleDetail(this, progressTime, progressImage.getUrl(), description,
                    progress));
        }
    }

    public boolean matchMember(Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean isInProgress(LocalDateTime now) {
        return progress.isInProgress(startTime, now);
    }

    public boolean isSuccess() {
        return this.progress.isSuccess();
    }

    public boolean isInDays(LocalDateTime now) {
        return now.isBefore(this.getStartTime().plusDays(DAYS));
    }

    public long calculateEndTime(LocalDateTime searchTime) {
        return this.progress.calculateEndTime(this.startTime, searchTime);
    }

    public List<CycleDetail> getCycleDetails() {
        return cycleDetails.stream()
                .sorted((detail1, detail2) ->
                        (int) ChronoUnit.MILLIS.between(detail2.getProgressTime(), detail1.getProgressTime()))
                .collect(Collectors.toList());
    }

    public boolean matchChallenge(Long challengeId) {
        return this.challenge.getId().equals(challengeId);
    }

    public int getInterval() {
        return progress.getCount() + 1;
    }

    public LocalDateTime getLatestProgressTime() {
        if (this.cycleDetails.isEmpty()) {
            return this.startTime;
        }
        return getCycleDetails().get(0).getProgressTime();
    }
}
