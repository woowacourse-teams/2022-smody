package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.CycleDetail;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleDetailResponse {

    private LocalDateTime progressTime;
    private String progressImage;
    private String description;

    public CycleDetailResponse(CycleDetail cycleDetail) {
        this.progressImage = cycleDetail.getProgressImage();
        this.progressTime = cycleDetail.getProgressTime();
        this.description = cycleDetail.getDescription();
    }
}
