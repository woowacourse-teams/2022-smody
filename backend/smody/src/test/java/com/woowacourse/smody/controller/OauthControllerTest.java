package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class OauthControllerTest extends ControllerTest {

    @DisplayName("구글 로그인에 성공하면 200을 응답한다.")
    @Test
    void loginGoogle() throws Exception {
        // given
        LoginResponse loginResponse = new LoginResponse("smodyAccessToken", true);
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
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"),
                                fieldWithPath("isNewMember").type(JsonFieldType.BOOLEAN).description("신규 회원인지 여부")
                        )));
    }
}
