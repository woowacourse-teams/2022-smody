package com.woowacourse.smody.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.domain.member.Nickname;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NicknameTest {

    @ParameterizedTest(name = "유효하지 않은 nickname - {0}")
    @ValueSource(strings = {"알", "12345678901", " 알파", "파 알", "알파쿤 "})
    void validateNickname(String invalidNickname) {
        // when then
        assertThatThrownBy(() -> new Nickname(invalidNickname))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_NICKNAME);
    }
}
