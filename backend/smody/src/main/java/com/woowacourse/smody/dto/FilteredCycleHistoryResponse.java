package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilteredCycleHistoryResponse {

    private Long cycleId;
    private Integer emojiIndex;
    private Integer colorIndex;
    private LocalDateTime startTime;
    private List<FilteredCycleDetailResponse> cycleDetails;
}
