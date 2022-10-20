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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cycle {

    public static final long DAYS = 3L;
    private static final int MAX_CYCLE_DETAILS_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @JoinColumn(name = "challenge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @OneToMany(mappedBy = "cycle", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @BatchSize(size = 10)
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
        if (this.cycleDetails.size() < MAX_CYCLE_DETAILS_SIZE) {
            this.cycleDetails.add(
                    new CycleDetail(this, progressTime, progressImage.getUrl(), description, progress)
            );
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

    public long calculateDeadLineToMillis(LocalDateTime searchTime) {
        return this.progress.calculateDeadLineToMillis(this.startTime, searchTime);
    }

    public List<CycleDetail> getCycleDetailsOrderByProgress() {
        return cycleDetails.stream()
                .sorted((detail1, detail2) ->
                        (int) ChronoUnit.MILLIS.between(detail2.getProgressTime(), detail1.getProgressTime()))
                .collect(Collectors.toList());
    }

    public int getInterval() {
        return progress.getCount() + 1;
    }

    public CycleDetail getLatestCycleDetail() {
        int lastIndex = this.cycleDetails.size() - 1;
        return getCycleDetailsOrderByProgress().get(lastIndex);
    }

    public LocalDateTime getLatestProgressTime() {
        if (this.cycleDetails.isEmpty()) {
            return this.startTime;
        }
        return getLatestCycleDetail().getProgressTime();
    }

    public int getCycleDetailCount() {
        return this.cycleDetails.size();
    }
}
