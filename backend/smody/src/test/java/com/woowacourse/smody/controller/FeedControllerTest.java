package com.woowacourse.smody.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.FeedResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class FeedControllerTest extends ControllerTest {

    @DisplayName("모든 피드를 조회할 때 200을 응답한다.")
    @Test
    void findAll() throws Exception {
        // given
        List<FeedResponse> feedResponses = List.of(
                new FeedResponse(1L, 1L, "토닉.jpg", "토닉", "인증.jpg", "인증설명", "2022-08-08T10:00:00",
                        "미라클 모닝", 5),
                new FeedResponse(2L, 2L, "빅터.jpg", "빅터", "인증.jpg", "인증설명", "2022-08-08T10:00:00",
                        "미라클 모닝", 4)
        );
        BDDMockito.given(feedQueryService.searchAll()).willReturn(feedResponses);

        // when
        ResultActions result = mockMvc.perform(get("/feeds?size=10&lastCycleDetailId=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(feedResponses)))
                .andDo(document("get-all-feeds", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].cycleDetailId").type(JsonFieldType.NUMBER).description("CycleDetail Id"),
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("Member Id"),
                                fieldWithPath("[].picture").type(JsonFieldType.STRING).description("프로필 이미지"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING).description("소개글"),
                                fieldWithPath("[].progressImage").type(JsonFieldType.STRING).description("인증 사진"),
                                fieldWithPath("[].progressTime").type(JsonFieldType.STRING).description("인증 시간"),
                                fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
                                fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수")
                        )));
    }
}
