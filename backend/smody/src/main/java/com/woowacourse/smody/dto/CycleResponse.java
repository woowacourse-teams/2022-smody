package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Cycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleResponse {

    private Long cycleId;
    private Long challengeId;

    public CycleResponse(Cycle cycle) {
        this.cycleId = cycle.getId();
        this.challengeId = cycle.getChallenge().getId();
    }
}
