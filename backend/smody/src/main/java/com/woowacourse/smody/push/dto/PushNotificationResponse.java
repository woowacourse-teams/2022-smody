package com.woowacourse.smody.push.dto;

import com.woowacourse.smody.push.domain.PushNotification;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PushNotificationResponse {

	private Long pushNotificationId;
	private String message;
	private LocalDateTime pushTime;
	private Long pathId;
	private String pushCase;

	public PushNotificationResponse(PushNotification pushNotification) {
		this.pushNotificationId = pushNotification.getId();
		this.message = pushNotification.getMessage();
		this.pushTime = pushNotification.getPushTime();
		this.pathId = pushNotification.getPathId();
		this.pushCase = pushNotification.getPushCase()
			.name()
			.toLowerCase();
	}
}
