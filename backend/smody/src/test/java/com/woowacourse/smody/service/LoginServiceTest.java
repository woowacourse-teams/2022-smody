package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @DisplayName("로그인 성공")
    @Test
    void login() {
        // TODO
    }

    @DisplayName("존재하지 않는 이메일로 인한 로그인 실패")
    @Test
    void login_notExistEmail() {
        // TODO
    }

    @DisplayName("일치하지 않는 비밀번호로 인한 로그인 실패")
    @Test
    void login_unmatchedPassword() {
        // TODO
    }
}
