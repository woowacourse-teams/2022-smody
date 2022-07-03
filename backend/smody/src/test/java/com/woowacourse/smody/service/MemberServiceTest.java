package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("회원가입이 정상적으로 진행")
    @Test
    void signUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("alpha@naver.com", "abcde12345", "손수건");

        // when
        SignUpResponse signUpResponse = memberService.signUp(signUpRequest);

        // then
        assertAll(
                () -> assertThat(signUpResponse.getId()).isNotNull(),
                () -> assertThat(signUpResponse.getEmail()).isEqualTo(signUpRequest.getEmail())
        );
    }

    @DisplayName("회원가입 시 이메일 형식이 맞지 않는 경우 예외 발생")
    @ParameterizedTest(name = "유효하지 않은 email - {0}")
    @ValueSource(strings = {
            "alpha@navercom", "alpha#naver.com",
            "alpha @naver.com", " alpha@naver.com",
            "alpha@naver.com ", "alpha@na ver.com",
            "alpha@naver .com", "alphanaver.com",
            "@naver.com", "com.naver@alpha",
            " ", ""})
    void signUp_invalidEmail(String invalidEmail) {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(invalidEmail, "abcde12345", "손수건");

        // when then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_EMAIL);
    }
}
