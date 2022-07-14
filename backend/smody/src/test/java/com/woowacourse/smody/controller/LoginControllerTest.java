package com.woowacourse.smody.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginControllerTest extends ControllerTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @DisplayName("로그인이 정상적으로 되었을 때 200을 반환한다.")
    @Test
    void loin_200() throws Exception {
        // TODO
    }

    @DisplayName("로그인이 정상적으로 되지않으면 401을 반환한다.")
    @Test
    void loin_401() throws Exception {
        // TODO
    }
}
