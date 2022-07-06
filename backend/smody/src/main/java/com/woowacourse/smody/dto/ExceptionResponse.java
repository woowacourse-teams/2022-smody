package com.woowacourse.smody.dto;

import com.woowacourse.smody.exception.ExceptionData;
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


