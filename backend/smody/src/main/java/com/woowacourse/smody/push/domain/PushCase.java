package com.woowacourse.smody.push.domain;

import lombok.Getter;

@Getter
public enum PushCase {

    SUBSCRIPTION,
    CHALLENGE,
    COMMENT,
    MENTION
}
