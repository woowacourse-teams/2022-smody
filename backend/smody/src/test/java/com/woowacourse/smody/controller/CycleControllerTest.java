package com.woowacourse.smody.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.StatResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;

public class CycleControllerTest extends ControllerTest {

	@DisplayName("사이클을 정상적으로 생성할 때 201을 응답한다.")
	@Test
	void create_201() throws Exception {
		// given
		Long cycleId = 1L;
		Map<String, Integer> param = Map.of("challengeId", 1);
		given(cycleService.create(any(TokenPayload.class), any(CycleRequest.class))).willReturn(cycleId);
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));

		// when
		ResultActions result = mockMvc.perform(post("/cycles")
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + token)
			.content(objectMapper.writeValueAsString(param)));

		// then
		result.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/cycles/" + cycleId))
			.andDo(document("create-cycle", HOST_INFO,
				preprocessResponse(prettyPrint()),

				requestFields(
					fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("Challenge Id")
				)));
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
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		ProgressResponse response = new ProgressResponse(2);
		given(cycleService.increaseProgress(any(TokenPayload.class), any(ProgressRequest.class)))
			.willReturn(response);

		// when
		ResultActions result = mockMvc.perform(post("/cycles/1/progress")
			.header("Authorization", "Bearer " + token));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)))
			.andDo(document("progress-cycle", HOST_INFO,
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("progressCount").type(JsonFieldType.NUMBER).description("사이클 진척도")
				)));
	}

	@DisplayName("사이클에 대한 진척도를 증가시킬 수 없으면 400을 응답한다.")
	@Test
	void increaseProgress_400() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
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
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
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
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		LocalDateTime now = LocalDateTime.now();
		List<CycleResponse> cycleResponses = List.of(
			new CycleResponse(1L, 1L, "미라클 모닝", 2, now, 3),
			new CycleResponse(2L, 2L, "오늘의 운동", 1, now, 3)
		);
		given(cycleQueryService.findInProgressOfMine(
			any(TokenPayload.class), any(LocalDateTime.class), any(Pageable.class)
		)).willReturn(cycleResponses);

		// when
		ResultActions result = mockMvc.perform(get("/cycles/me?page=0&size=5")
			.header("Authorization", "Bearer " + token));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(cycleResponses)))
			.andDo(document("get-inProgress-cycle-mine", HOST_INFO,
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("[].cycleId").type(JsonFieldType.NUMBER).description("사이클 Id"),
					fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("챌린지 Id"),
					fieldWithPath("[].challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
					fieldWithPath("[].progressCount").type(JsonFieldType.NUMBER).description("사이클 진척도"),
					fieldWithPath("[].startTime").type(JsonFieldType.STRING).description("사이클 시작 시간"),
					fieldWithPath("[].successCount").type(JsonFieldType.NUMBER).description("성공 횟수")
				)));
	}

	@DisplayName("id로 사이클 조회 시 200을 응답한다.")
	@Test
	void findById_200() throws Exception {
		// given
		long cycleId = 1L;
		CycleResponse cycleResponse = new CycleResponse(
			cycleId, 1L, "미라클 모닝", 2, LocalDateTime.now(), 3);
		given(cycleQueryService.findById(cycleId))
			.willReturn(cycleResponse);

		// when
		ResultActions result = mockMvc.perform(get("/cycles/" + cycleId));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(cycleResponse)))
			.andDo(document("get-cycle", HOST_INFO,
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("cycleId").type(JsonFieldType.NUMBER).description("사이클 Id"),
					fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("챌린지 Id"),
					fieldWithPath("challengeName").type(JsonFieldType.STRING).description("챌린지 이름"),
					fieldWithPath("progressCount").type(JsonFieldType.NUMBER).description("사이클 진척도"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("사이클 시작 시간"),
					fieldWithPath("successCount").type(JsonFieldType.NUMBER).description("성공 횟수")
				)));
	}

	@DisplayName("존재하지 않은 id로 사이클 조회 시 404를 응답한다.")
	@Test
	void findById_404() throws Exception {
		// given
		long cycleId = 1L;
		given(cycleQueryService.findById(cycleId))
			.willThrow(new BusinessException(ExceptionData.NOT_FOUND_CYCLE));

		// when
		ResultActions result = mockMvc.perform(get("/cycles/" + cycleId));

		// then
		result.andExpect(status().isNotFound());
	}

	@DisplayName("나의 전체 사이클 수와 성공 사이클 수를 조회 시 200을 응답한다.")
	@Test
	void searchStat() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		StatResponse statResponse = new StatResponse(35, 5);
		given(cycleQueryService.searchStat(any(TokenPayload.class)))
			.willReturn(statResponse);

		// when
		ResultActions result = mockMvc.perform(get("/cycles/me/stat")
			.header("Authorization", "Bearer " + token));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(
				objectMapper.writeValueAsString(statResponse)))
			.andDo(document("get-cycle-stat",
				HOST_INFO,
				preprocessResponse(prettyPrint()),

				responseFields(
					fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 도전 횟수"),
					fieldWithPath("successCount").type(JsonFieldType.NUMBER).description("총 성공 횟수")
				)));
	}
}
