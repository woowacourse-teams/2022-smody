package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.dto.EmailRequest;
import com.woowacourse.smody.dto.ExceptionResponse;
import com.woowacourse.smody.dto.NicknameRequest;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @DisplayName("회원 가입의 요청과 응답이 정상적으로 동작한다.")
    @Test
    void signUp() throws Exception {
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

    @DisplayName("이메일 중복검사 요청과 응답이 정상적으로 동작한다.")
    @Test
    void checkDuplicatedEmail() throws Exception {
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

    @DisplayName("닉네임 중복검사 요청과 응답이 정상적으로 동작한다.")
    @Test
    void checkDuplicatedNickname() throws Exception {
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
