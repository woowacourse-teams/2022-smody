package com.woowacourse.smody.cycle.dto;

import com.woowacourse.smody.cycle.domain.CycleDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilteredCycleDetailResponse {

    private Long cycleDetailId;
    private String progressImage;

    public FilteredCycleDetailResponse(CycleDetail cycleDetail) {
        this.cycleDetailId = cycleDetail.getId();
        this.progressImage = cycleDetail.getProgressImage();
    }
}
