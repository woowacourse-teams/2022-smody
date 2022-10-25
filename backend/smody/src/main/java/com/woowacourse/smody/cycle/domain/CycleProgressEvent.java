package com.woowacourse.smody.cycle.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CycleProgressEvent {

    private final Cycle cycle;
}
