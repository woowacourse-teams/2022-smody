package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Integer emoji;
    private Integer color;

    public InProgressCycleResponse(Cycle cycle, Integer successCount) {
        this.cycleId = cycle.getId();
        this.challengeId = cycle.getChallenge().getId();
        this.challengeName = cycle.getChallenge().getName();
        this.progressCount = cycle.getProgress().getCount();
        this.startTime = cycle.getStartTime();
        this.successCount = successCount;
        this.emoji = cycle.getChallenge().getEmoji();
        this.color = cycle.getChallenge().getColor();
    }
}
