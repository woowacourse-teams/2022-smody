package com.woowacourse.smody.ranking.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_column_in_ranking_period",
                columnNames = {"startDate", "duration"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankingPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_period_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Duration duration;

    public RankingPeriod(LocalDateTime startDate, Duration duration) {
        this.startDate = startDate;
        this.duration = duration;
    }

    public boolean isBeforeEndTime(LocalDateTime time) {
        LocalDateTime endTime = startDate.plusDays(duration.getDays());
        return time.isBefore(endTime);
    }
}
