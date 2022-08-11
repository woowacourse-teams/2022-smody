package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.woowacourse.smody.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChallengeControllerTest extends ControllerTest {

    @DisplayName("비회원이 모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount_unAuthorized() throws Exception {
        // given
        List<ChallengesResponse> challengesResponses = List.of(
                new ChallengesResponse(1L, "스모디 방문하기", 3, false, 0 ,1),
                new ChallengesResponse(2L, "미라클 모닝", 5, false, 1, 2)
        );

        given(challengeQueryService.findAllWithChallengerCount(any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(challengesResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges?page=0&size=10"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengesResponses)))
                .andDo(document("get-all-challenges", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING)
                                        .description("Challenge 이름"),
                                fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("[].emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("[].colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("회원이 모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount_authorized() throws Exception {
        // given
        List<ChallengesResponse> challengesResponses = List.of(
                new ChallengesResponse(1L, "스모디 방문하기", 3, true, 0, 1),
                new ChallengesResponse(2L, "미라클 모닝", 5, false, 1, 2)
        );
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        given(challengeQueryService.findAllWithChallengerCount(any(TokenPayload.class), any(LocalDateTime.class),
                any(Pageable.class)))
                .willReturn(challengesResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/auth?page=0&size=10")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengesResponses)))
                .andDo(document("get-all-challenges-auth", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING)
                                        .description("Challenge 이름"),
                                fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("[].emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("[].colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("나의 성공한 챌린지를 페이지네이션 없이 조회 시 200을 응답한다.")
    @Test
    void searchSuccessOfMine() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        List<SuccessChallengeResponse> successChallengeResponses = List.of(
                new SuccessChallengeResponse(1L, "스모디 방문하기", 2, 0, 1),
                new SuccessChallengeResponse(2L, "미라클 모닝", 1, 1, 2)
        );

        given(challengeQueryService.searchSuccessOfMine(any(TokenPayload.class), any(Pageable.class)))
                .willReturn(successChallengeResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/me?page=0&size=10")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(successChallengeResponses)))
                .andDo(document("get-my-success-challenges", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING)
                                        .description("Challenge 이름"),
                                fieldWithPath("[].successCount").type(JsonFieldType.NUMBER).description("성공 횟수"),
                                fieldWithPath("[].emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("[].colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("비회원이 챌린지 하나를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithChallengerCount_unAuthorized() throws Exception {
        // given
        ChallengeResponse challengeResponse =
                new ChallengeResponse(1L, "스모디 방문하기", 3, false, "스모디 방문하기 입니다", 0, 1);
        given(challengeQueryService.findOneWithChallengerCount(any(LocalDateTime.class), eq(1L)))
                .willReturn(challengeResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponse)))
                .andDo(document("get-challenge", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                                fieldWithPath("challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                                fieldWithPath("challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("챌린지 소개"),
                                fieldWithPath("emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("회원이 챌린지 하나를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithChallengerCount_authorized() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        ChallengeResponse challengeResponse =
                new ChallengeResponse(1L, "스모디 방문하기", 3, true, "스모디 방문하기 입니다", 0, 1);
        given(challengeQueryService.findOneWithChallengerCount(
                any(TokenPayload.class), any(LocalDateTime.class), eq(1L))
        ).willReturn(challengeResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1/auth")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponse)))
                .andDo(document("get-challenge-auth", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("Challenge Id"),
                                fieldWithPath("challengeName").type(JsonFieldType.STRING).description("Challenge 이름"),
                                fieldWithPath("challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("챌린지 소개"),
                                fieldWithPath("emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("챌린지에 참가한 회원들을 조회할 때 200을 응답한다.")
    @Test
    void findAllChallengers() throws Exception {
        // given
        List<ChallengersResponse> challengersResponse = List.of(
                new ChallengersResponse(1L, "조조그린", 1, "picture1.jpg", "조조그린입니다"),
                new ChallengersResponse(2L, "알파", 0, "picture2.jpg", "알파입니다")
        );
        given(challengeQueryService.findAllChallengers(eq(1L))
        ).willReturn(challengersResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/1/challengers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengersResponse)))
                .andDo(document("get-challengers", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("멤버 닉네임"),
                                fieldWithPath("[].progressCount").type(JsonFieldType.NUMBER).description("인증 횟수"),
                                fieldWithPath("[].picture").type(JsonFieldType.STRING).description("프로필 사진"),
                                fieldWithPath("[].introduction").type(JsonFieldType.STRING).description("자기 소개")
                        )));
    }

    @DisplayName("챌린지를 생성할 때 201을 응답한다.")
    @Test
    void create_201() throws Exception {
        // given
        Long challengeId = 1L;
        ChallengeRequest challengeRequest = new ChallengeRequest("1일 1포스팅 하기", "1일 1포스팅을 실천하는 챌린지입니다", 0, 1);
        given(challengeService.create(any(ChallengeRequest.class))).willReturn(challengeId);
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        // when
        ResultActions result = mockMvc.perform(post("/challenges")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(challengeRequest)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/challenges/" + challengeId))
                .andDo(document("create-challenge", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("챌린지 소개"),
                                fieldWithPath("emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("비회원이 챌린지를 이름으로 검색할 때 200을 응답한다.")
    @Test
    void searchByName_200() throws Exception {
        // given
        List<ChallengesResponse> challengesResponse = List.of(
                new ChallengesResponse(
                        1L, "알고리즘 공부하기", 15, false, 0, 1
                ),
                new ChallengesResponse(
                        5L, "외국어 공부하기", 7, false, 1, 2
                )
        );
        given(challengeQueryService.searchByName(eq("공부"))).willReturn(challengesResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/searched?name=공부"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengesResponse)))
                .andDo(document("search-challenge", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge ID"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
                                fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("[].emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("[].colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }

    @DisplayName("회원이 챌린지를 이름으로 검색할 때 200을 응답한다.")
    @Test
    void searchByName_auth_200() throws Exception {
        // given
        List<ChallengesResponse> challengesResponse = List.of(
                new ChallengesResponse(
                        1L, "알고리즘 공부하기", 15, true, 0, 1
                ),
                new ChallengesResponse(
                        5L, "외국어 공부하기", 7, false, 1, 2
                )
        );
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        given(challengeQueryService.searchByName(any(TokenPayload.class), eq("공부"))).willReturn(challengesResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/searched/auth?name=공부")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengesResponse)))
                .andDo(document("search-challenge-auth", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("Challenge ID"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
                                fieldWithPath("[].challengerCount").type(JsonFieldType.NUMBER).description("참여자 수"),
                                fieldWithPath("[].isInProgress").type(JsonFieldType.BOOLEAN).description("참여 여부"),
                                fieldWithPath("[].emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("[].colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스")
                        )));
    }
}
