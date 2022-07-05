package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CycleControllerTest extends ControllerTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("사이클을 정상적으로 생성할 때 201을 응답한다.")
    @Test
    void create_201() throws Exception {
        // given
        Long cycleId = 1L;

        CycleRequest request = new CycleRequest(LocalDateTime.now(), 1L);
        given(cycleService.create(any(TokenPayload.class), any(CycleRequest.class))).willReturn(cycleId);
        String token = jwtTokenProvider.createToken(new TokenPayload(1L, "손수건"));
        // when
        ResultActions result = mockMvc.perform(post("/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cycles/" + cycleId));
    }

    @DisplayName("유효하지 않은 토큰으로 사이클을 생성할 때 403을 응답한다.")
    @Test
    void create_403() throws Exception {
        // given
        CycleRequest request = new CycleRequest(LocalDateTime.now(), 1L);

        // when
        ResultActions result = mockMvc.perform(post("/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "invalidToken")
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isForbidden());
    }
}
