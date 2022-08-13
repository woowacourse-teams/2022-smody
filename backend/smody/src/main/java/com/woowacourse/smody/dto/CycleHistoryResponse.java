package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.CycleDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleHistoryResponse {

    private Long cycleId;
    private List<CycleDetailResponse> cycleDetails;
}
