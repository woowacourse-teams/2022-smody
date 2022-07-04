package com.woowacourse.smody.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionData {

    DUPLICATED_EMAIL(1001, "이미 존재하는 이메일입니다.", 400),
    DUPLICATED_NICKNAME(1002, "이미 존재하는 닉네임입니다.", 400),
    INVALID_EMAIL(1003, "유효하지 않은 이메일입니다.", 400),
    INVALID_NICKNAME(1004, "유효하지 않은 닉네임입니다.", 400),
    INVALID_PASSWORD(1005, "유효하지 않은 비밀번호입니다.", 400),
    INVALID_LOGIN(2001, "이메일 혹은 비밀번호가 일치하지 않습니다.", 401);

    private final int code;
    private final String message;
    private final int statusCode;
}
