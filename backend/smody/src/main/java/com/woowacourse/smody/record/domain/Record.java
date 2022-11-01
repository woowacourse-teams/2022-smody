package com.woowacourse.smody.record.domain;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_record", columnNames = {"member_id", "challenge_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Getter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @JoinColumn(name = "challenge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @Column(nullable = false)
    private Integer successCount;

    @Column(nullable = false)
    private Integer progressCount;

    @Column(nullable = false)
    private LocalDateTime deadLineTime;

    @Column(nullable = false)
    private boolean isSuccess;

    public Record(Member member, Challenge challenge, Integer successCount,
                  Integer progressCount, LocalDateTime deadLineTime, boolean isSuccess) {
        validateSuccessCount(successCount);
        validateProgressCount(progressCount);
        this.member = member;
        this.challenge = challenge;
        this.successCount = successCount;
        this.progressCount = progressCount;
        this.deadLineTime = deadLineTime;
        this.isSuccess = isSuccess;
    }

    private void validateSuccessCount(Integer successCount) {
        if (successCount == null || successCount < 0) {
            throw new BusinessException(ExceptionData.INVALID_SUCCESS_COUNT);
        }
    }

    private void validateProgressCount(Integer progressCount) {
        if (progressCount == null || progressCount < 0) {
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_COUNT);
        }
    }

    public void changeDeadLine(LocalDateTime startTime, Long days) {
        if (days == null || days > 3L || days < 1L) {
            throw new BusinessException(ExceptionData.CANNOT_UPDATE_DEADLINE);
        }
        this.deadLineTime = startTime.plusDays(days);
    }

    public void toNotSuccess() {
        if (this.isSuccess) {
            this.isSuccess = false;
        }
    }

    public void toSuccess() {
        if (!this.isSuccess) {
            this.isSuccess = true;
        }
    }
}
