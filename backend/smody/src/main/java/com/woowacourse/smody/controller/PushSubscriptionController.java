package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.dto.VapidPublicKeyResponse;
import com.woowacourse.smody.service.PushSubscriptionService;
import com.woowacourse.smody.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web-push")
@RequiredArgsConstructor
public class PushSubscriptionController {

    private final PushSubscriptionService pushSubscriptionService;
    private final WebPushService webPushService;

    @GetMapping("/public-key")
    public ResponseEntity<VapidPublicKeyResponse> sendPublicKey() {
        return ResponseEntity.ok(new VapidPublicKeyResponse(webPushService.getPublicKey()));
    }

    @PostMapping("/subscribe")
    @RequiredLogin
    public ResponseEntity<Void> subscribe(@LoginMember TokenPayload tokenPayload,
                                          @RequestBody SubscriptionRequest subscription) {
        pushSubscriptionService.subscribe(tokenPayload, subscription);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    @RequiredLogin
    public ResponseEntity<Void> unSubscribe(@LoginMember TokenPayload tokenPayload,
                                            @RequestBody UnSubscriptionRequest unSubscription) {
        pushSubscriptionService.unSubscribe(tokenPayload, unSubscription);
        return ResponseEntity.noContent().build();
    }
}
