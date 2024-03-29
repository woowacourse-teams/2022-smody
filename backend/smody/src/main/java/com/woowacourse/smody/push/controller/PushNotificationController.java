package com.woowacourse.smody.push.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.push.dto.PushNotificationResponse;
import com.woowacourse.smody.push.service.PushNotificationApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push-notifications")
@RequiredArgsConstructor
public class PushNotificationController {

    private final PushNotificationApiService pushNotificationApiService;

    // TODO-api 수정(/push-notifications/me)
    @GetMapping
    @RequiredLogin
    public ResponseEntity<List<PushNotificationResponse>> findCompleteNotificationsByMe(
            @LoginMember TokenPayload tokenPayload
    ) {
        return ResponseEntity.ok(pushNotificationApiService.findCompleteNotificationsByMe(tokenPayload));
    }

    @DeleteMapping("{id}")
    @RequiredLogin
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        pushNotificationApiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // TODO-api 수정(/push-notifications/mention)
    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> createMentionNotification(@LoginMember TokenPayload tokenPayload,
                                                          @RequestBody MentionNotificationRequest mentionNotificationRequest) {
        pushNotificationApiService.createMentionNotification(tokenPayload, mentionNotificationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    @RequiredLogin
    public ResponseEntity<Void> deleteCompleteNotificationsByMe(@LoginMember TokenPayload tokenPayload) {
        pushNotificationApiService.deleteCompleteNotificationsByMe(tokenPayload);
        return ResponseEntity.noContent().build();
    }
}
