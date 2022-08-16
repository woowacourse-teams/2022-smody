package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilteredCycleHistoryResponse {

    private Long cycleId;
    private List<CycleDetailResponse> cycleDetails;
}
