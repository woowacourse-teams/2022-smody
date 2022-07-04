package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.ExceptionResponse;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class LoginControllerTest extends ControllerTest{

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @DisplayName("로그인이 정상적으로 되었을 때 200을 반환한다.")
    @Test
    void loin_200() throws Exception {
        // given
        LoginResponse loginResponse = new LoginResponse(NICKNAME, "accessToken");
        given(loginService.login(any(LoginRequest.class)))
                .willReturn(loginResponse);

        // when
        ResultActions result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest(EMAIL, PASSWORD))));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)));
    }

    @DisplayName("로그인이 정상적으로 되지않으면 401을 반환한다.")
    @Test
    void loin_401() throws Exception {
        // given
        given(loginService.login(any(LoginRequest.class)))
                .willThrow(new BusinessException(ExceptionData.INVALID_LOGIN));

        // when
        ResultActions result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest(EMAIL, "unmatchedPassword0"))));

        // then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(new ExceptionResponse(ExceptionData.INVALID_LOGIN)
                        )));
    }
}
