package com.woowacourse.smody.cycle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CycleRequest {

    private LocalDateTime startTime;
    private Long challengeId;
}
