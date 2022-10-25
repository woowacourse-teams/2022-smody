package com.woowacourse.smody.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.exception.api.GithubApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    @DisplayName("accessToken이 일치하지 않으면 Github에서 401 응답 코드를 반환한다")
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
