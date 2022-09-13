package com.woowacourse.smody.challenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeRequest;
import com.woowacourse.smody.challenge.dto.ChallengeResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.challenge.dto.ChallengersResponse;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.support.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChallengeControllerTest extends ControllerTest {

    @DisplayName("비회원이 모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount_unAuthorized() throws Exception {
        // given
        List<ChallengeTabResponse> challengeTabRespons = List.of(
                new ChallengeTabResponse(1L, "스모디 방문하기", 3, false, 0, 1),
                new ChallengeTabResponse(2L, "미라클 모닝", 5, false, 1, 2)
        );

        given(challengeQueryService.findAllWithChallengerCount(any(LocalDateTime.class), any(PagingParams.class)))
                .willReturn(challengeTabRespons);

        // when
        ResultActions result = mockMvc.perform(get("/challenges?size=10&cursorId=7"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeTabRespons)))
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
        List<ChallengeTabResponse> challengeTabRespons = List.of(
                new ChallengeTabResponse(1L, "스모디 방문하기", 3, true, 0, 1),
                new ChallengeTabResponse(2L, "미라클 모닝", 5, false, 1, 2)
        );
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        given(challengeQueryService.findAllWithChallengerCount(any(TokenPayload.class), any(LocalDateTime.class),
                any(PagingParams.class)))
                .willReturn(challengeTabRespons);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/auth?size=10&cursorId=7")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeTabRespons)))
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

    @DisplayName("나의 참가한 챌린지 이력을 조회 시 200을 응답한다.")
    @Test
    void searchOfMine() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        List<ChallengeOfMineResponse> challengeOfMineRespons = List.of(
                new ChallengeOfMineResponse(1L, "스모디 방문하기", 2, 0, 1),
                new ChallengeOfMineResponse(2L, "미라클 모닝", 1, 1, 2),
                new ChallengeOfMineResponse(3L, "오늘의 운동", 0, 2, 0)
        );

        given(challengeQueryService.searchOfMine(any(TokenPayload.class), any(PagingParams.class)))
                .willReturn(challengeOfMineRespons);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/me?size=10&cursorId=7")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(challengeOfMineRespons)))
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
        given(challengeQueryService.findWithChallengerCount(any(LocalDateTime.class), eq(1L)))
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
        given(challengeQueryService.findWithChallengerCount(
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
        List<ChallengeTabResponse> challengeTabResponse = List.of(
                new ChallengeTabResponse(
                        1L, "알고리즘 공부하기", 15, false, 0, 1
                ),
                new ChallengeTabResponse(
                        5L, "외국어 공부하기", 7, false, 1, 2
                )
        );
        given(challengeQueryService.findAllWithChallengerCount
                (any(LocalDateTime.class), any(PagingParams.class))).willReturn(challengeTabResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges?size=10&cursorId=9&filter=공부"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeTabResponse)))
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
        List<ChallengeTabResponse> challengeTabResponse = List.of(
                new ChallengeTabResponse(
                        1L, "알고리즘 공부하기", 15, true, 0, 1
                ),
                new ChallengeTabResponse(
                        5L, "외국어 공부하기", 7, false, 1, 2
                )
        );
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        given(challengeQueryService.findAllWithChallengerCount(
                any(TokenPayload.class), any(LocalDateTime.class), any(PagingParams.class)))
                .willReturn(challengeTabResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/auth?size=10&cursorId=9&filter=공부")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeTabResponse)))
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

    @DisplayName("회원이 자신이 참가한 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findOneWithMine_auth_200() throws Exception {
        // given
        ChallengeHistoryResponse challengeHistoryResponse = new ChallengeHistoryResponse(
                "알고리즘 풀기", "알고리즘 풀기 챌린지입니다", 0, 1,
                10, 40);
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        given(challengeQueryService.findWithMine(
                any(TokenPayload.class), eq(1L)))
                .willReturn(challengeHistoryResponse);

        // when
        ResultActions result = mockMvc.perform(get("/challenges/me/1")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeHistoryResponse)))
                .andDo(document("find-challenge-mine", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("챌린지 소개"),
                                fieldWithPath("emojiIndex").type(JsonFieldType.NUMBER).description("이모지의 인덱스"),
                                fieldWithPath("colorIndex").type(JsonFieldType.NUMBER).description("배경색상의 인덱스"),
                                fieldWithPath("successCount").type(JsonFieldType.NUMBER).description("성공 횟수"),
                                fieldWithPath("cycleDetailCount").type(JsonFieldType.NUMBER).description("인증 횟수")
                        )));
    }
}
