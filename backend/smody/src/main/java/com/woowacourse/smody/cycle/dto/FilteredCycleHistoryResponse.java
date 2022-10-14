package com.woowacourse.smody.cycle.dto;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

    public FilteredCycleHistoryResponse(Cycle cycle, Challenge challenge) {
        this.cycleId = cycle.getId();
        this.emojiIndex = challenge.getEmojiIndex();
        this.colorIndex = challenge.getColorIndex();
        this.startTime = cycle.getStartTime();
        this.cycleDetails = cycle.getCycleDetailsOrderByProgress().stream()
                .map(FilteredCycleDetailResponse::new)
                .collect(Collectors.toList());
    }
}
