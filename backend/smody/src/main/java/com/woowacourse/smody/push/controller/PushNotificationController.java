package com.woowacourse.smody.push.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.push.dto.PushNotificationResponse;
import com.woowacourse.smody.push.service.PushNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> saveNotification(@LoginMember TokenPayload tokenPayload,
                                                 @RequestBody MentionNotificationRequest mentionNotificationRequest) {
        pushNotificationService.saveNotification(tokenPayload, mentionNotificationRequest);
        return ResponseEntity.ok().build();
    }
}
