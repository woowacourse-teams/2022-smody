package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    @DisplayName("사이클에 대한 진척도를 증가시키면 200을 응답한다.")
    @Test
    void increaseProgress_200() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L, "손수건"));
        ProgressResponse response = new ProgressResponse(2);
        given(cycleService.increaseProgress(any(TokenPayload.class), any(ProgressRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/cycles/1/progress")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("사이클에 대한 진척도를 증가시킬 수 없으면 400을 응답한다.")
    @Test
    void increaseProgress_400() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L, "손수건"));
        given(cycleService.increaseProgress(any(TokenPayload.class), any(ProgressRequest.class)))
                .willThrow(new BusinessException(ExceptionData.INVALID_PROGRESS_TIME));

        // when
        ResultActions result = mockMvc.perform(post("/cycles/1/progress")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("사이클에 대한 권한이 없으면 403을 응답한다.")
    @Test
    void increaseProgress_403() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L, "손수건"));
        given(cycleService.increaseProgress(any(TokenPayload.class), any(ProgressRequest.class)))
                .willThrow(new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER));

        // when
        ResultActions result = mockMvc.perform(post("/cycles/1/progress")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("자신의 진행 중인 사이클을 조회 시 200을 응답한다.")
    @Test
    void findAllInProgressOfMine_200() throws Exception {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Challenge challenge1 = new Challenge("공부");
        String token = jwtTokenProvider.createToken(new TokenPayload(1L, "손수건"));
        List<CycleResponse> cycleResponses = List.of(
                new CycleResponse(new Cycle(member1, challenge1, Progress.NOTHING, LocalDateTime.now())),
                new CycleResponse(new Cycle(member2, challenge1, Progress.NOTHING, LocalDateTime.now()))
        );
        given(cycleService.findAllInProgressOfMine(
                any(TokenPayload.class), any(LocalDateTime.class), any(Pageable.class)))
                .willReturn(cycleResponses);

        // when
        ResultActions result = mockMvc.perform(get("/cycles/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cycleResponses)));
    }

    @DisplayName("id로 사이클 조회 시 200을 응답한다.")
    @Test
    void findById_200() throws Exception {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");
        Challenge challenge = new Challenge("공부");
        CycleResponse cycleResponse = new CycleResponse(
                new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now())
        );
        long cycleId = 1L;
        given(cycleService.findById(cycleId))
                .willReturn(cycleResponse);

        // when
        ResultActions result = mockMvc.perform(get("/cycles/" + cycleId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cycleResponse)));
    }

    @DisplayName("존재하지 않은 id로 사이클 조회 시 404를 응답한다.")
    @Test
    void findById_404() throws Exception {
        // given
        long cycleId = 1L;
        given(cycleService.findById(cycleId))
                .willThrow(new BusinessException(ExceptionData.NOT_FOUND_CYCLE));

        // when
        ResultActions result = mockMvc.perform(get("/cycles/" + cycleId));

        // then
        result.andExpect(status().isNotFound());
    }
}
