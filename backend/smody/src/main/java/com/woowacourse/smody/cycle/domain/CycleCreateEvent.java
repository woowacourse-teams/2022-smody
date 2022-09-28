package com.woowacourse.smody.cycle.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CycleCreateEvent {

	private final Cycle cycle;
}
