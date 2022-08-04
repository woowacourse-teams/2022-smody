package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleResponse {

    private Long cycleId;
    private Long challengeId;
    private String challengeName;
    private Integer progressCount;
    private LocalDateTime startTime;
    private Integer successCount;

    private List<CycleDetailResponse> cycleDetails;

    public CycleResponse(Cycle cycle, Integer successCount) {
        this.cycleId = cycle.getId();
        this.challengeId = cycle.getChallenge().getId();
        this.challengeName = cycle.getChallenge().getName();
        this.progressCount = cycle.getProgress().getCount();
        this.startTime = cycle.getStartTime();
        this.successCount = successCount;
        this.cycleDetails = cycle.getCycleDetails().stream()
                .map(CycleDetailResponse::new)
                .collect(Collectors.toList());
    }
}
