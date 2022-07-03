package com.woowacourse.smody.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionData {

    INVALID_EMAIL(1003, "유효하지 않은 이메일입니다.", 400);

    private final int code;
    private final String message;
    private final int statusCode;
}
