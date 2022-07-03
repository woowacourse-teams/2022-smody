package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LoginServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private LoginService loginService;

    @Autowired
    private MemberService memberService;

    @DisplayName("로그인 성공")
    @Test
    void login() {
        // given
        memberService.signUp(new SignUpRequest(EMAIL, PASSWORD, NICKNAME));

        // when
        LoginResponse loginResponse = loginService.login(new LoginRequest(EMAIL, PASSWORD));

        // then
        assertAll(
                () -> assertThat(loginResponse.getAccessToken()).isNotNull(),
                () -> assertThat(loginResponse.getNickname()).isEqualTo(NICKNAME)
        );
    }

    @DisplayName("존재하지 않는 이메일로 인한 로그인 실패")
    @Test
    void login_notExistEmail() {
        // given
        memberService.signUp(new SignUpRequest(EMAIL, PASSWORD, NICKNAME));

        // when then
        assertThatThrownBy(() -> loginService.login(new LoginRequest("notExist@naver.com", PASSWORD)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_LOGIN);
    }

    @DisplayName("일치하지 않는 비밀번호로 인한 로그인 실패")
    @Test
    void login_unmatchedPassword() {
        // given
        memberService.signUp(new SignUpRequest(EMAIL, PASSWORD, NICKNAME));

        // when then
        assertThatThrownBy(() -> loginService.login(new LoginRequest(EMAIL, "unmatchedPassword0")))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_LOGIN);
    }
}
