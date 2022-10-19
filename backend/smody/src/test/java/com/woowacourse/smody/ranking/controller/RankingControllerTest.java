package com.woowacourse.smody.ranking.controller;

import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.support.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class RankingControllerTest extends ControllerTest {

    @DisplayName("전체 랭킹 기간 조회")
    @Test
    void findAllRankingPeriods() throws Exception {
        // given
        List<RankingPeriodResponse> rankingPeriodResponses = List.of(
                new RankingPeriodResponse(
                        1L,
                        LocalDateTime.of(2022, 7, 1, 17, 0, 0),
                        "week"
                ),
                new RankingPeriodResponse(
                        1L,
                        LocalDateTime.of(2022, 7, 8, 17, 0, 0),
                        "week"
                )
        );
        given(rankingApiService.findAllRankingPeriod(new PagingParams("startDate:desc", 10)))
                .willReturn(rankingPeriodResponses);

        // when
        ResultActions result = mockMvc.perform(get("/ranking-periods?sort=startDate:desc&size=10"));

        // then
        String jsonContent = objectMapper.writeValueAsString(rankingPeriodResponses);
        result.andExpect(status().isOk())
                .andExpect(content().json(jsonContent))
                .andDo(document("get-all-ranking-periods", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].rankingPeriodId").type(JsonFieldType.NUMBER)
                                        .description("RankingPeriod Id"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("시작 날짜"),
                                fieldWithPath("[].duration").type(JsonFieldType.STRING).description("기간")
                        )));
    }

    @DisplayName("전체 랭킹 활동 조회")
    @Test
    void findAllRankingActivities() throws Exception {
        // given
        List<RankingActivityResponse> responses = List.of(
                new RankingActivityResponse(
                        1, 1L, "조조그린", "안녕하세요", "picture.png", 100
                ),
                new RankingActivityResponse(
                        2, 2L, "더즈", "안녕하세요", "picture.png", 99
                )
        );

        given(rankingApiService.findAllRankingActivityByPeriodId(1L))
                .willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/ranking-periods/1/ranking-activities"));

        // then
        String jsonContent = objectMapper.writeValueAsString(responses);
        result.andExpect(status().isOk())
                .andExpect(content().json(jsonContent))
                .andDo(document("get-ranking-activities", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].ranking").type(JsonFieldType.NUMBER).description("순위"),
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("member ID"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[].introduction").type(JsonFieldType.STRING).description("회원 소개글"),
                                fieldWithPath("[].picture").type(JsonFieldType.STRING).description("회원 프로필 사진"),
                                fieldWithPath("[].point").type(JsonFieldType.NUMBER).description("점수")
                        )));
    }

    @DisplayName("나의 랭킹 활동 조회")
    @Test
    void findRankingActivityOfMine() throws Exception {
        // given
        RankingActivityResponse response = new RankingActivityResponse(
                1, 1L, "조조그린", "안녕하세요", "picture.png", 100
        );

        TokenPayload payload = new TokenPayload(조조그린_ID);
        String token = jwtTokenProvider.createToken(payload);
        given(rankingApiService.findActivityOfMine(payload, 1L))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/ranking-periods/1/ranking-activities/me")
                .header("Authorization", "Bearer " + token));

        // then
        String jsonContent = objectMapper.writeValueAsString(response);
        result.andExpect(status().isOk())
                .andExpect(content().json(jsonContent))
                .andDo(document("get-ranking-activity-of-mine", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ranking").type(JsonFieldType.NUMBER).description("순위"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("member ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("회원 소개글"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("회원 프로필 사진"),
                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("점수")
                        )));
    }
}
