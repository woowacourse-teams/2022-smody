package com.woowacourse.smody.push.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PushSubscribeEvent {

	private final PushSubscription pushSubscription;
}
