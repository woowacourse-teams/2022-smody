package com.woowacourse.smody.push.strategy;

import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;

public interface PushStrategy {

	void push(Object entity);

	PushCase getPushCase();

	PushNotification buildNotification(Object entity);
}
