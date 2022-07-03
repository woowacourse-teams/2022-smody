package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.EmailRequest;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private MemberService memberService;

    @DisplayName("회원가입이 정상적으로 진행")
    @Test
    void signUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(EMAIL, PASSWORD, NICKNAME);

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
        SignUpRequest signUpRequest = new SignUpRequest(invalidEmail, PASSWORD, NICKNAME);

        // when then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_EMAIL);
    }

    @DisplayName("회원가입 시 비밀번호 형식이 맞지 않는 경우 예외 발생")
    @ParameterizedTest(name = "유효하지 않은 password - {0}")
    @ValueSource(strings = {
            "12345678a", "12345678901234567890a",
            "qwertyuiop", "1234567890",
            "12345678 a", " 12345678a",
            "12345678a ", "!12345678a",
            "가12345678a", "12345678a가"})
    void signUp_invalidPassword(String invalidPassword) {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(EMAIL, invalidPassword, NICKNAME);

        // when then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PASSWORD);
    }

    @ParameterizedTest(name = "유효하지 않은 nickname - {0}")
    @ValueSource(strings = {"알", "12345678901", " 알파", "파 알", "알파쿤 "})
    void signUp_invalidNickname(String invalidNickname) {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(EMAIL, PASSWORD, invalidNickname);

        // when then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_NICKNAME);
    }

    @DisplayName("이메일이 중복되면 예외가 발생한다.")
    @Test
    void checkDuplicatedEmail() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(EMAIL, PASSWORD, NICKNAME);
        memberService.signUp(signUpRequest);

        // when then
        assertThatThrownBy(() -> memberService.checkDuplicatedEmail(new EmailRequest(EMAIL)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DUPLICATED_EMAIL);
    }

}
