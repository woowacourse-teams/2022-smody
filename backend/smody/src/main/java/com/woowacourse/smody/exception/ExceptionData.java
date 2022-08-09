package com.woowacourse.smody.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionData {

    INVALID_INTRODUCTION_LENGTH(1001, "소개글 길이는 30 자 이내입니다.", 400),

    INVALID_TOKEN(2002, "유효하지 않은 토큰입니다.", 403),
    UNAUTHORIZED_MEMBER(2003, "인가되지 않은 회원입니다.", 403),
    INVALID_AUTHORIZATION_CODE(2004, "OAuth 인증 코드가 올바르지 않습니다.", 403),

    ALREADY_SUCCESS(3001, "이미 성공한 사이클입니다.", 400),
    INVALID_PROGRESS_TIME(3002, "인증할 수 있는 시간이 아닙니다.", 400),
    DUPLICATE_IN_PROGRESS_CHALLENGE(3003, "이미 진행중인 챌린지입니다.", 400),
    INVALID_START_TIME(3004, "유효하지 않은 시작시간입니다.", 400),

    NOT_FOUND_MEMBER(4001, "존재하지 않는 회원입니다.", 404),
    NOT_FOUND_CHALLENGE(4002, "존재하지 않는 챌린지입니다.", 404),
    NOT_FOUND_CYCLE(4003, "존재하지 않는 사이클입니다.", 404),

    EMPTY_IMAGE(5001, "이미지의 바이트코드가 비었습니다", 400),

    INVALID_CYCLE_DETAIL_DESCRIPTION(6001, "인증 설명 형식이 올바르지 않습니다.", 400),

    INVALID_CHALLENGE_DESCRIPTION(7001, "챌린지 소개 형식이 올바르지 않습니다.", 400),
    DUPLICATE_NAME(7002, "중복된 챌린지 이름입니다.", 400),
    INVALID_CHALLENGE_NAME(7003, "챌린지 이름 형식이 올바르지 않습니다.", 400),
    INVALID_SEARCH_NAME(7004, "검색 이름 형식이 올바르지 않습니다.", 400),

    WEB_PUSH_ERROR(8001, "웹 푸시 라이브러리 관련 예외입니다.", 400),

    AUTHORIZATION_SERVER_ERROR(9001, "인가 관련 서버 내부의 오류입니다.", 500),
    IMAGE_UPLOAD_ERROR(9002, "이미지 업로드 관련 서버 내부의 오류입니다.", 500);

    private final int code;
    private final String message;
    private final int statusCode;
}
