package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.EmailRequest;
import com.woowacourse.smody.dto.ExceptionResponse;
import com.woowacourse.smody.dto.NicknameRequest;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MemberControllerTest extends ControllerTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @DisplayName("회원 가입이 정상적으로 되었을 경우 201을 응답한다.")
    @Test
    void signUp_201() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(EMAIL, PASSWORD, NICKNAME);
        SignUpResponse signUpResponse = new SignUpResponse(1L, signUpRequest.getEmail());

        given(memberService.signUp(any(SignUpRequest.class)))
                .willReturn(signUpResponse);

        // when
        ResultActions result = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(objectMapper.writeValueAsString(signUpResponse)));
    }

    @DisplayName("이메일이 중복되지 않았을 경우 200을 응답한다.")
    @Test
    void checkDuplicatedEmail_200() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/members/emails/checkDuplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new EmailRequest(EMAIL))));

        // then
        result.andExpect(status().isOk());
    }

    @DisplayName("이메일 중복됐을 경우 400을 응답한다.")
    @Test
    void checkDuplicatedEmail_400() throws Exception {
        // given
        doThrow(new BusinessException(ExceptionData.DUPLICATED_EMAIL))
                .when(memberService).checkDuplicatedEmail(any(EmailRequest.class));

        // when
        ResultActions result = mockMvc.perform(post("/members/emails/checkDuplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new EmailRequest(EMAIL))));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(new ExceptionResponse(ExceptionData.DUPLICATED_EMAIL)
                        )));
    }

    @DisplayName("닉네임 중복검사를 통과 시 200을 응답한다.")
    @Test
    void checkDuplicatedNickname_200() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/members/nicknames/checkDuplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new NicknameRequest(NICKNAME))));

        // then
        result.andExpect(status().isOk());
    }

    @DisplayName("닉네임 중복검사를 통과하지 못할 시 400을 응답한다.")
    @Test
    void checkDuplicatedNickname_400() throws Exception {
        // given
        doThrow(new BusinessException(ExceptionData.DUPLICATED_NICKNAME))
                .when(memberService).checkDuplicatedNickname(any(NicknameRequest.class));

        // when
        ResultActions result = mockMvc.perform(post("/members/nicknames/checkDuplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new NicknameRequest(NICKNAME))));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(new ExceptionResponse(ExceptionData.DUPLICATED_NICKNAME)
                        )));
    }
}
