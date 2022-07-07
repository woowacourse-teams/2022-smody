package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.ChallengeResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class ChallengeControllerTest extends ControllerTest {

    @DisplayName("모든 챌린지를 조회할 때 200을 응답한다.")
    @Test
    void findAllWithChallengerCount() throws Exception {
        List<ChallengeResponse> challengeResponses = List.of(
                new ChallengeResponse(1L, "공부", 3),
                new ChallengeResponse(2L, "운동", 5)
        );
        given(challengeService.findAllWithChallengerCount(any(LocalDateTime.class)))
                .willReturn(challengeResponses);

        // when
        ResultActions result = mockMvc.perform(get("/challenges"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(challengeResponses)));
    }
}
