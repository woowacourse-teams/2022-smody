package com.woowacourse.smody.dto;

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
}
