package com.woowacourse.smody.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.dto.VapidPublicKeyResponse;
import com.woowacourse.smody.service.PushSubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/web-push")
@RequiredArgsConstructor
public class PushSubscriptionController {

	private final PushSubscriptionService pushSubscriptionService;

	@GetMapping("/public-key")
	public ResponseEntity<VapidPublicKeyResponse> sendPublicKey() {
		return ResponseEntity.ok(new VapidPublicKeyResponse(pushSubscriptionService.getPublicKey()));
	}

	@PostMapping("/subscribe")
	public ResponseEntity<Void> subscribe(@LoginMember TokenPayload tokenPayload,
		@RequestBody SubscriptionRequest subscription) {
		pushSubscriptionService.subscribe(tokenPayload, subscription);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/unsubscribe")
	public ResponseEntity<Void> unSubscribe(@LoginMember TokenPayload tokenPayload,
		@RequestBody UnSubscriptionRequest unSubscription) {
		pushSubscriptionService.unSubscribe(tokenPayload, unSubscription);
		return ResponseEntity.noContent().build();
	}
}
