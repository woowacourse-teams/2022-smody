package com.woowacourse.smody.cycle.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleRequest {

    private LocalDateTime startTime;
    private Long challengeId;
}
