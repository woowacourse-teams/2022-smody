package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.TokenPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class MemberControllerTest extends ControllerTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void searchMyInfo() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        MemberResponse memberResponse = new MemberResponse(EMAIL, NICKNAME, PICTURE);
        given(memberService.searchMyInfo(any(TokenPayload.class)))
                .willReturn(memberResponse);

        // when
        ResultActions result = mockMvc.perform(get("/members/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberResponse)));
    }
}
