package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.PushNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@DeleteMapping("{id}")
	@RequiredLogin
	public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
		pushNotificationService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
