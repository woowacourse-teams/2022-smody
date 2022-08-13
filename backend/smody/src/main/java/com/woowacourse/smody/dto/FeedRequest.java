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
public class FeedRequest {

    private String sort;
    private Integer size;
    private Long lastCycleDetailId;

    public Pageable toPageRequest() {
        if (size == null || size < 1) {
            return PageRequest.of(0, 10, SortSelection.findByParameter(sort).getSort());
        }
        return PageRequest.of(0, size, SortSelection.findByParameter(sort).getSort());
    }
}
