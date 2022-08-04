package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Cycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InProgressCycleResponse {

    private Long cycleId;
    private Long challengeId;
    private String challengeName;
    private Integer progressCount;
    private LocalDateTime startTime;
    private Integer successCount;

    public InProgressCycleResponse(Cycle cycle, Integer successCount) {
        this.cycleId = cycle.getId();
        this.challengeId = cycle.getChallenge().getId();
        this.challengeName = cycle.getChallenge().getName();
        this.progressCount = cycle.getProgress().getCount();
        this.startTime = cycle.getStartTime();
        this.successCount = successCount;
    }
}