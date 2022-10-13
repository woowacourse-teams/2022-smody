package com.woowacourse.smody.push.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.dto.UnSubscriptionRequest;
import com.woowacourse.smody.push.dto.VapidPublicKeyResponse;
import com.woowacourse.smody.push.service.PushSubscriptionApiService;
import com.woowacourse.smody.push.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-push")
@RequiredArgsConstructor
public class PushSubscriptionController {

    private final PushSubscriptionApiService pushSubscriptionApiService;
    private final WebPushService webPushService;

    @GetMapping("/public-key")
    public ResponseEntity<VapidPublicKeyResponse> sendPublicKey() {
        return ResponseEntity.ok(new VapidPublicKeyResponse(webPushService.getPublicKey()));
    }

    @PostMapping("/subscribe")
    @RequiredLogin
    public ResponseEntity<Void> subscribe(@LoginMember TokenPayload tokenPayload,
                                          @RequestBody SubscriptionRequest subscription) {
        pushSubscriptionApiService.subscribe(tokenPayload, subscription);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    @RequiredLogin
    public ResponseEntity<Void> unSubscribe(@LoginMember TokenPayload tokenPayload,
                                            @RequestBody UnSubscriptionRequest unSubscription) {
        pushSubscriptionApiService.unSubscribe(tokenPayload, unSubscription);
        return ResponseEntity.noContent().build();
    }
}
