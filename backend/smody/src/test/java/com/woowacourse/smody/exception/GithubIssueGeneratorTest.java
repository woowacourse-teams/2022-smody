package com.woowacourse.smody.exception;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.woowacourse.smody.support.IntegrationTest;

class GithubIssueGeneratorTest extends IntegrationTest {

	private static final String REPO_URL = "https://api.github.com/repos/woowacourse-teams/2022-smody/issues";

	@Autowired
	private GithubIssueGenerator githubIssueGenerator;

	@DisplayName("Exception에 따라 깃헙 이슈 생성 api를 보낸다.")
	@Test
	void create() {
		// given
		RuntimeException exception = new RuntimeException("Unexpected Error");

		// when
		githubIssueGenerator.create(exception);

		// then
		verify(restTemplate).exchange(
			eq(REPO_URL),
			eq(HttpMethod.POST),
			any(HttpEntity.class),
			eq(String.class)
		);
	}
}
