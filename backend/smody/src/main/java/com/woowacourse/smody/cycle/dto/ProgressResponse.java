package com.woowacourse.smody.cycle.dto;

import com.woowacourse.smody.cycle.domain.Cycle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProgressResponse {

    private Integer progressCount;
    private Long cycleDetailId;

    public ProgressResponse(Cycle cycle) {
        this.progressCount = cycle.getProgress().getCount();
        this.cycleDetailId = cycle.getLatestCycleDetail().getId();
    }
}
