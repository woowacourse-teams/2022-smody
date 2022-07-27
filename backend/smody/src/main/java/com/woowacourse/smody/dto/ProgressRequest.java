package com.woowacourse.smody.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProgressRequest {

    private Long cycleId;
    private LocalDateTime progressTime;
}
