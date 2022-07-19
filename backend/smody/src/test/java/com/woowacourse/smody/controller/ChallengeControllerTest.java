package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
        ResultActions result = mockMvc.perform(get("/challenges"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponses)));
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
        ResultActions result = mockMvc.perform(get("/challenges/auth")
                        .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponses)));
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
        ResultActions result = mockMvc.perform(get("/challenges/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(successChallengeResponses)));
    }

    @DisplayName("챌린지 하나를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithChallengerCount() throws Exception {
        // given
        ChallengeResponse challengeResponse =
                new ChallengeResponse(1L, "스모디 방문하기", 3, false);
        given(challengeService.findOneWithChallengerCount(any(LocalDateTime.class), eq(1L)))
                .willReturn(challengeResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponse)));
    }
}
