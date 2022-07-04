package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.ExceptionResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException businessException) {
        ExceptionData exceptionData = businessException.getExceptionData();
        ExceptionResponse exceptionResponse = new ExceptionResponse(exceptionData);
        return ResponseEntity.status(exceptionData.getStatusCode())
                .body(exceptionResponse);
    }
}
