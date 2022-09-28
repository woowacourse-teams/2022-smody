package com.woowacourse.smody.ranking.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.support.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class RankingControllerTest extends ControllerTest {

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
        given(rankingPeriodService.findAllLatestOrder()).willReturn(rankingPeriodResponses);

        // when
        ResultActions result = mockMvc.perform(get("/ranking-periods"));

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
}
