package com.woowacourse.smody.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.PushNotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/push-notifications")
@RequiredArgsConstructor
public class PushNotificationController {

	private final PushNotificationService pushNotificationService;

	@GetMapping
	@RequiredLogin
	public ResponseEntity<List<PushNotificationResponse>> searchNotificationsOfMine(
		@LoginMember TokenPayload tokenPayload) {
		return ResponseEntity.ok(pushNotificationService.searchNotificationsOfMine(tokenPayload));
	}
}
