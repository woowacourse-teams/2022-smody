package com.woowacourse.smody.push.dto;

import com.woowacourse.smody.push.domain.PushNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PushRequest {

    private String message;
    private Long pathId;
    private String pushCase;

    public PushRequest(PushNotification pushNotification) {
        this.message = pushNotification.getMessage();
        this.pathId = pushNotification.getPathId();
        this.pushCase = pushNotification.getPushCase()
                .name()
                .toLowerCase();
    }
}
