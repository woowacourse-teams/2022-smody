package com.woowacourse.smody.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailTest {

    @ParameterizedTest(name = "유효하지 않은 email - {0}")
    @ValueSource(strings = {
            "alpha@navercom", "alpha#naver.com",
            "alpha @naver.com", " alpha@naver.com",
            "alpha@naver.com ", "alpha@na ver.com",
            "alpha@naver .com", "alphanaver.com",
            "@naver.com", "com.naver@alpha",
            " ", ""})
    void validateEmail(String invalidEmail) {
        // when then
        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_EMAIL);
    }
}
