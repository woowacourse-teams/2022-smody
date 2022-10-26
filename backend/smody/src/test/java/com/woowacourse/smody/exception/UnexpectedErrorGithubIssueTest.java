package com.woowacourse.smody.exception;

import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.smody.exception.api.GithubApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class UnexpectedErrorGithubIssueTest {

    private ControllerAdvice controllerAdvice;
    private MockEnvironment environment;
    private GithubApi githubApi;
    private Exception exception;

    @BeforeEach
    void init() {
        githubApi = mock(GithubApi.class);
        environment = new MockEnvironment();
        controllerAdvice = new ControllerAdvice(githubApi, environment);

        exception = new RuntimeException("예상치 못한 예외 발생");
    }

    @DisplayName("local 환경에서는 깃헙 이슈를 생성하지 않는다.")
    @Test
    void handleUnexpectedException_local() {
        // given
        environment.setActiveProfiles("local");

        // when
        controllerAdvice.handleUnExpectedException(exception);

        // then
        verify(githubApi, never()).create(exception);
    }

    @DisplayName("prod 환경에서는 깃헙 이슈를 생성한다.")
    @Test
    void handleUnexpectedException_prod() {
        // given
        environment.setActiveProfiles("prod");

        // when
        controllerAdvice.handleUnExpectedException(exception);

        // then
        verify(githubApi).create(exception);
    }
}
