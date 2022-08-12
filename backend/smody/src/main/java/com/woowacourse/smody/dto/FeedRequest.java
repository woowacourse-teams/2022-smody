package com.woowacourse.smody.dto;

import com.woowacourse.smody.repository.SortSelection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FeedRequest {

    private String sort;
    private int size;
    private Long cycleDetailId;

    public Pageable toPageRequest() {
        return PageRequest.of(0, size, SortSelection.findByParameter(sort).getSort());
    }
}
