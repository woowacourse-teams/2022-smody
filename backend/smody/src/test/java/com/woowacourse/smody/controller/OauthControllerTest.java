package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;

class OauthControllerTest extends ControllerTest {

    @Test
    void loginGoogle() throws Exception {
        // given
        LoginResponse loginResponse = new LoginResponse("smodyAccessToken");
        LoginRequest loginRequest = new LoginRequest("email", "nickname", "picture");
        given(googleApi.requestToken(any(String.class)))
                .willReturn(loginRequest);
        given(oauthService.login(any(LoginRequest.class)))
                .willReturn(loginResponse);

        // when
        ResultActions result = mockMvc.perform(get("/oauth/login/google?code=authorizationCode"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)))
            .andDo(document("login-google", HOST_INFO,
                preprocessResponse(prettyPrint()),
                responseFields(fieldWithPath("accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"))
            ));
    }
}
