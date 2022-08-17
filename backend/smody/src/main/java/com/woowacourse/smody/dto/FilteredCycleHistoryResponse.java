package com.woowacourse.smody.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilteredCycleHistoryResponse {

    private Long cycleId;
    private List<CycleDetailResponse> cycleDetails;
}
