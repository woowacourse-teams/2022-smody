package com.woowacourse.smody.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;

class PushNotificationControllerTest extends ControllerTest {

	@DisplayName("회원의 안 읽은 알림을 조회하면 200을 응답한다.")
	@Test
	void findNotifications() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));

		PushNotificationResponse pushNotificationResponse1 = new PushNotificationResponse(
			1L, "조조그린님 스모디 알림이 구독되었습니다..",
			LocalDateTime.now(), 1L, "subscription"
		);
		PushNotificationResponse pushNotificationResponse2 = new PushNotificationResponse(
			2L, "미라클 모닝 챌린지 인증까지 얼마 안남았어요~",
			LocalDateTime.now(), 2L, "challenge"
		);
		List<PushNotificationResponse> responses = List.of(pushNotificationResponse1,
			pushNotificationResponse2);
		given(pushNotificationService.searchNotificationsOfMine(any()))
			.willReturn(responses);

		// when
		ResultActions result = mockMvc.perform(get("/push-notifications")
			.header("Authorization", "Bearer " + token));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(responses)))
			.andDo(document("get-my-notifications", HOST_INFO,
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("[].pushNotificationId").type(JsonFieldType.NUMBER).description("알림 id"),
					fieldWithPath("[].message").type(JsonFieldType.STRING).description("알림 내용"),
					fieldWithPath("[].pushTime").type(JsonFieldType.STRING).description("알림 보낸 시간"),
					fieldWithPath("[].pathId").type(JsonFieldType.NUMBER).description("알림과 관련된 리소스 id"),
					fieldWithPath("[].pushCase").type(JsonFieldType.STRING).description("알림 발생 케이스 (맥락)")
				)
			));
	}

	@DisplayName("알림을 삭제하면 204 응답을 반환한다.")
	@Test
	void deleteNotification() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));

		// when
		ResultActions result = mockMvc.perform(delete("/push-notifications/1")
			.header("Authorization", "Bearer " + token));

		// then
		result.andExpect(status().isNoContent())
			.andDo(document("delete-notification", HOST_INFO,
				preprocessResponse(prettyPrint())
			));
	}
}
