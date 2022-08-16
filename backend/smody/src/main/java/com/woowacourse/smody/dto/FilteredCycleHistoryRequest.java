package com.woowacourse.smody.dto;

import com.woowacourse.smody.repository.SortSelection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilteredCycleHistoryRequest {

    private String filter;
    private Integer size;
    private Long lastCycleId;

    private Long challengeId;

    public Pageable toPageRequest() {
        if (size == null || size < 1) {
            return PageRequest.of(0, 10, SortSelection.CYCLE_LATEST.getSort());
        }
        return PageRequest.of(0, size, SortSelection.CYCLE_LATEST.getSort());
    }
}
