package com.woowacourse.smody.cycle.dto;

import com.woowacourse.smody.cycle.domain.Progress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProgressResponse {

    private Integer progressCount;

    public ProgressResponse(Progress progress) {
        this.progressCount = progress.getCount();
    }
}
