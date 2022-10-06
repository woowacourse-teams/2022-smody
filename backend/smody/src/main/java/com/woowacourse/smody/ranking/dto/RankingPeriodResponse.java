package com.woowacourse.smody.ranking.dto;

import com.woowacourse.smody.ranking.domain.RankingPeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingPeriodResponse {

    private Long rankingPeriodId;
    private LocalDateTime startDate;
    private String duration;

    public RankingPeriodResponse(RankingPeriod rankingPeriod) {
        this.rankingPeriodId = rankingPeriod.getId();
        this.startDate = rankingPeriod.getStartDate();
        this.duration = rankingPeriod.getDuration()
                .name()
                .toLowerCase();
    }
}
