package com.woowacourse.smody.dto;

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
public class ChallengeOfMineRequest {

    private String filter;

    private Integer page;
    private Integer size;

    public Pageable toPageRequest() {
        if (size == null || size < 1) {
            return PageRequest.of(page, 10);
        }
        return PageRequest.of(page, size);
    }
}
