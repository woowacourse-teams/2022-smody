package com.woowacourse.smody.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequest;
import com.woowacourse.smody.dto.VapidPublicKeyResponse;

class PushSubscriptionControllerTest extends ControllerTest {

	@DisplayName("Vapid 공개키를 조회할 때 200을 응답한다.")
	@Test
	void getPublicKey() throws Exception {
		// given
		String publicKey = "BNzzfdcBcThU27FcGve6F3GF6He2Fro82ZMuOLga9fukatLMlaKB6GdO-82loi6W4iGdPQZAp_4HLgST8z5of_E";
		given(pushSubscriptionService.getPublicKey())
			.willReturn(publicKey);

		// when
		ResultActions result = mockMvc.perform(get("/web-push/public-key"));

		// then
		result.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(new VapidPublicKeyResponse(publicKey))))
			.andDo(document("get-vapid-public-key", HOST_INFO,
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("publicKey").type(JsonFieldType.STRING).description("vapid 공개키")
				)));
	}

	@DisplayName("알림 구독을 요청하면 200을 응답한다.")
	@Test
	void subscribe() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth"
		);

		// when
		ResultActions result = mockMvc.perform(post("/web-push/subscribe")
			.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(subscriptionRequest)));

		// then
		result.andExpect(status().isOk())
			.andDo(document("web-push-subscribe", HOST_INFO,
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("endpoint").type(JsonFieldType.STRING).description("알림을 보낼 브라우저 주소"),
					fieldWithPath("keys.p256dh").type(JsonFieldType.STRING).description("암호화에 사용되는 공개 키"),
					fieldWithPath("keys.auth").type(JsonFieldType.STRING).description("암호화에 사용되는 비밀 키")
				)));
	}

	@DisplayName("알림 구독 해제을 요청하면 204를 응답한다.")
	@Test
	void unSubscribe() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		UnSubscriptionRequest unSubscriptionRequest = new UnSubscriptionRequest("endpoint-link");

		// when
		ResultActions result = mockMvc.perform(post("/web-push/unsubscribe")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(unSubscriptionRequest)));

		// then
		result.andExpect(status().isNoContent())
			.andDo(document("web-push-unsubscribe", HOST_INFO,
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("endpoint").type(JsonFieldType.STRING).description("알림 해제할 브라우저 주소")
				)));
	}
}
