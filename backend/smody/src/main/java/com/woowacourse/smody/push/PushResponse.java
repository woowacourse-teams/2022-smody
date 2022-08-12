package com.woowacourse.smody.push;

import com.woowacourse.smody.domain.PushNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PushResponse {

	private String message;

	public PushResponse(PushNotification pushNotification) {
		this.message = pushNotification.getMessage();
	}
}
