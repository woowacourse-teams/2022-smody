package com.woowacourse.smody.exception;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.woowacourse.smody.exception.api.GithubApi;
import com.woowacourse.smody.exception.dto.ExceptionResponse;

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
        log.info("[비즈니스 예외 발생] 에러 코드 : {}, 메세지 : {}", exceptionData.getCode(), exceptionData.getMessage());
        if (exceptionData.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
                && isProdProfile()) {
            githubApi.create(businessException);
        }
        return ResponseEntity.status(exceptionData.getStatusCode())
                .body(new ExceptionResponse(exceptionData));
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> handleBindingException() {
        ExceptionData exceptionData = ExceptionData.REQUEST_BINDING_ERROR;
        return ResponseEntity.status(exceptionData.getStatusCode())
            .body(new ExceptionResponse(exceptionData));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUnExpectedException(Exception exception) {
        log.error("[예상치 못한 예외 발생] ", exception);
        if (isProdProfile()) {
            githubApi.create(exception);
        }
        return ResponseEntity.internalServerError()
                .body(ExceptionUtils.extractStackTrace(exception));
    }

    private boolean isProdProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(env -> env.equalsIgnoreCase(ISSUE_CREATE_PROFILE));
    }
}
