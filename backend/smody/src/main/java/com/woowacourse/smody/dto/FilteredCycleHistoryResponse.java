package com.woowacourse.smody.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
