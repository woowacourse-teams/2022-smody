package com.woowacourse.smody.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PasswordTest {

    @ParameterizedTest(name = "유효하지 않은 password - {0}")
    @ValueSource(strings = {
            "12345678a", "12345678901234567890a",
            "qwertyuiop", "1234567890",
            "12345678 a", " 12345678a",
            "12345678a ", "!12345678a",
            "가12345678a", "12345678a가"})
    void validatePassword(String invalidPassword) {
        // when then
        assertThatThrownBy(() -> new Password(invalidPassword))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PASSWORD);
    }
}
