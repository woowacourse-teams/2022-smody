package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.ExceptionResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException businessException) {
        ExceptionData exceptionData = businessException.getExceptionData();
        ExceptionResponse exceptionResponse = new ExceptionResponse(exceptionData);
        log.info("[비즈니스 예외 발생] 에러 코드 : {}, 메세지 : {}", exceptionData.getCode(), exceptionData.getMessage());
        return ResponseEntity.status(exceptionData.getStatusCode())
                .body(exceptionResponse);
    }
}
