package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChallengeControllerTest extends ControllerTest {

    @DisplayName("비회원이 모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount_unAuthorized() throws Exception {
        // given
        List<ChallengeResponse> challengeResponses = List.of(
                new ChallengeResponse(1L, "스모디 방문하기", 3, false),
                new ChallengeResponse(2L, "미라클 모닝", 5, false)
        );

        given(challengeService.findAllWithChallengerCount(any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(challengeResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges?page=0&size=10"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(challengeResponses)))
            .andDo(document("get-all-challenges",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(),prettyPrint()),

                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                    fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                    fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                    fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부")
                )));
    }

    @DisplayName("회원이 모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount_authorized() throws Exception {
        // given
        List<ChallengeResponse> challengeResponses = List.of(
                new ChallengeResponse(1L, "스모디 방문하기", 3, true),
                new ChallengeResponse(2L, "미라클 모닝", 5, false)
        );
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        given(challengeService.findAllWithChallengerCount(any(TokenPayload.class), any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(challengeResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/auth?page=0&size=10")
                        .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponses)))
            .andDo(document("get-all-challenges-auth",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(),prettyPrint()),

                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                    fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                    fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                    fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부")
                )));
    }

    @DisplayName("나의 성공한 챌린지를 페이지네이션 없이 조회 시 200을 응답한다.")
    @Test
    void searchSuccessOfMine() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        List<SuccessChallengeResponse> successChallengeResponses = List.of(
                new SuccessChallengeResponse(1L, "스모디 방문하기", 2),
                new SuccessChallengeResponse(2L, "미라클 모닝", 1)
        );

        given(challengeService.searchSuccessOfMine(any(TokenPayload.class), any(Pageable.class)))
                .willReturn(successChallengeResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/me?page=0&size=10")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(successChallengeResponses)))
            .andDo(document("get-my-success-challenges",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(),prettyPrint()),

                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                    fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                    fieldWithPath("[].successCount").type(JsonFieldType.NUMBER).description("성공 횟 수")
                )));
    }

    @DisplayName("비회원이 챌린지 하나를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithChallengerCount_unAuthorized() throws Exception {
        // given
        ChallengeResponse challengeResponse =
                new ChallengeResponse(1L, "스모디 방문하기", 3, false);
        given(challengeService.findOneWithChallengerCount(any(LocalDateTime.class), eq(1L)))
                .willReturn(challengeResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponse)))
            .andDo(document("get-challenge",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(),prettyPrint()),

                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                    fieldWithPath("challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                    fieldWithPath("challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                    fieldWithPath("isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부")
                )));
    }

    @DisplayName("회원이 챌린지 하나를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithChallengerCount_authorized() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        ChallengeResponse challengeResponse =
                new ChallengeResponse(1L, "스모디 방문하기", 3, true);
        given(challengeService.findOneWithChallengerCount(any(TokenPayload.class), any(LocalDateTime.class), eq(1L)))
                .willReturn(challengeResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1/auth")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponse)))
            .andDo(document("get-challenge-auth",
                preprocessRequest(modifyUris()
                    .scheme("http")
                    .host("www.smody.co.kr")
                    .removePort(),prettyPrint()),

                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                    fieldWithPath("challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                    fieldWithPath("challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                    fieldWithPath("isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부")
                )));
    }
}
