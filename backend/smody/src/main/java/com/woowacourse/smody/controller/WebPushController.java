package com.woowacourse.smody.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.SubscriptionRequestDto;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequestDto;
import com.woowacourse.smody.dto.VapidPublicKeyResponseDto;
import com.woowacourse.smody.service.WebPushService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/web-push")
@RequiredArgsConstructor
public class WebPushController {

	private final WebPushService webPushService;

	@GetMapping("/public-key")
	public ResponseEntity<VapidPublicKeyResponseDto> sendPublicKey() {
		return ResponseEntity.ok(new VapidPublicKeyResponseDto(webPushService.getPublicKey()));
	}

	@PostMapping("/subscribe")
	public ResponseEntity<Void> subscribe(@LoginMember TokenPayload tokenPayload,
		@RequestBody SubscriptionRequestDto subscription) {
		webPushService.subscribe(tokenPayload, subscription);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/unsubscribe")
	public ResponseEntity<Void> unSubscribe(@LoginMember TokenPayload tokenPayload,
		@RequestBody UnSubscriptionRequestDto unSubscription) {
		webPushService.unSubscribe(tokenPayload, unSubscription);
		return ResponseEntity.noContent().build();
	}
}
