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

    INVALID_LOGIN(2001, "이메일 혹은 비밀번호가 일치하지 않습니다.", 401),
    INVALID_TOKEN(2002, "유효하지 않은 토큰입니다.", 403),
    UNAUTHORIZED_MEMBER(2003, "인가되지 않은 회원입니다.", 403),

    ALREADY_SUCCESS(3001, "이미 성공한 사이클입니다.", 400),
    INVALID_PROGRESS_TIME(3002, "인증할 수 있는 시간이 아닙니다.", 400),
    DUPLICATE_IN_PROGRESS_CHALLENGE(3003, "이미 진행중인 챌린지입니다.", 400),
    INVALID_START_TIME(3004, "유효하지 않은 시작시간입니다.", 400),

    NOT_FOUND_MEMBER(4001, "존재하지 않는 회원입니다.", 404),
    NOT_FOUND_CHALLENGE(4002, "존재하지 않는 챌린지입니다.", 404),
    NOT_FOUND_CYCLE(4003, "존재하지 않는 사이클입니다.", 404),

    AUTHORIZATION_SERVER_ERROR(9001, "인가 관련 서버 내부의 오류입니다.", 500);

    private final int code;
    private final String message;
    private final int statusCode;
}
