package com.woowacourse.smody.exception;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.support.ControllerTest;

class UnexpectedErrorGithubIssueIntegrationTest extends ControllerTest {

	@DisplayName("테스트 환경에서는 깃헙 이슈를 생성하지 않는다.")
	@Test
	void unexpectedException() throws Exception {
		// given
		String token = jwtTokenProvider.createToken(new TokenPayload(1L));
		RuntimeException exception = new RuntimeException("예상치 못한 예외");
		given(memberService.searchMyInfo(any(TokenPayload.class)))
			.willThrow(exception);

		// when
		mockMvc.perform(get("/members/me")
			.header("Authorization", "Bearer " + token));

		// then
		verify(githubApi, never()).create(exception);
	}
}
