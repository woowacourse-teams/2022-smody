package com.woowacourse.smody.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExceptionResponse {

    private Integer code;
    private String message;

    public ExceptionResponse(ExceptionData exceptionData) {
        this.code = exceptionData.getCode();
        this.message = exceptionData.getMessage();
    }
}


