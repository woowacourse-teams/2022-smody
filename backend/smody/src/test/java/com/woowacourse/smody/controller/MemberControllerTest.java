package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.TokenPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class MemberControllerTest extends ControllerTest {

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void searchMyInfo() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        MemberResponse memberResponse = new MemberResponse("alpha@naver.com", "손수건", "사진");
        given(memberService.searchMyInfo(any(TokenPayload.class)))
                .willReturn(memberResponse);

        // when
        ResultActions result = mockMvc.perform(get("/members/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberResponse)))
            .andDo(document("get-my-info",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),

                responseFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("picture").type(JsonFieldType.STRING).description("사진"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                )));
    }
}
