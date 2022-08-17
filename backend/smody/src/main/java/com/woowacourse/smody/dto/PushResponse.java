package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.PushNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PushResponse {

	private String message;
	private Long pathId;
	private String pushCase;

	public PushResponse(PushNotification pushNotification) {
		this.message = pushNotification.getMessage();
		this.pathId = pushNotification.getPathId();
		this.pushCase = pushNotification.getPushCase()
			.name()
			.toLowerCase();
	}
}
