package com.woowacourse.smody.ranking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingPeriodResponse {

    private Long rankingPeriodId;
    private LocalDateTime startDate;
    private String duration;
}
