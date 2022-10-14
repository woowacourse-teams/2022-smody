package com.woowacourse.smody.exception;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {

    private static final String ISSUE_CREATE_PROFILE = "prod";

    private final GithubApi githubApi;
    private final Environment environment;

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException businessException) {
        ExceptionData exceptionData = businessException.getExceptionData();
        ExceptionResponse exceptionResponse = new ExceptionResponse(exceptionData);
        log.info("[비즈니스 예외 발생] 에러 코드 : {}, 메세지 : {}", exceptionData.getCode(), exceptionData.getMessage());
        return ResponseEntity.status(exceptionData.getStatusCode())
            .body(exceptionResponse);
    }

    @ExceptionHandler
    public void handleUnExpectedException(Exception exception) {
        log.error("[예상치 못한 예외 발생] ", exception);
        if (isProd(environment.getActiveProfiles())) {
            githubApi.create(exception);
        }
    }

    private boolean isProd(String[] activeProfiles) {
        return Arrays.stream(activeProfiles)
            .anyMatch(env -> env.equalsIgnoreCase(ISSUE_CREATE_PROFILE));
    }
}
