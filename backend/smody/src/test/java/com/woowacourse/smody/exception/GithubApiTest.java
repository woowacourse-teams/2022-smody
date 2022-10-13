package com.woowacourse.smody.exception;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

class GithubApiTest {

	private static final String REPO_URL = "https://api.github.com/repos/woowacourse-teams/2022-smody/issues";

	private final RuntimeException exception = new RuntimeException("Unexpected Error");

	@DisplayName("Exception에 따라 깃헙 이슈 생성 api를 보낸다.")
	@Test
	void create() {
		// given
		RestTemplate restTemplate = mock(RestTemplate.class);
		GithubApi githubApi = new GithubApi(new ObjectMapper(), restTemplate);

		// when
		githubApi.create(exception);

		// then
		verify(restTemplate).exchange(
			eq(REPO_URL),
			eq(HttpMethod.POST),
			any(HttpEntity.class),
			eq(String.class)
		);
	}

	@DisplayName("Exception에 따라 깃헙 이슈 생성 api를 보낸다.")
	@Test
	void create_fail() {
		// given
		GithubApi githubApi = new GithubApi(new ObjectMapper(), new RestTemplate());

		// when then
		assertThatThrownBy(() -> githubApi.create(exception))
			.isInstanceOf(HttpClientErrorException.class)
			.hasMessage("401 Unauthorized: [no body]");
	}
}
