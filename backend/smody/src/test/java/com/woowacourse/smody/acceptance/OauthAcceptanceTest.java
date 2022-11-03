package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.auth.dto.ValidAuthResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class OauthAcceptanceTest extends AcceptanceTest {

    @DisplayName("구글 링크를 요청한다.")
    @Test
    void oauth_link_google() {
        // when
        ExtractableResponse<Response> response = 구글_링크_요청();

        // then
        assertAll(
            OK_응답(response),
            () -> assertThat(response.body().toString()).isNotEmpty()
        );
    }

    @DisplayName("유효한 토큰인지 검증한다.")
    @Test
    void get_oauth_check() {
        // when
        ExtractableResponse<Response> response = 토큰_검증_요청(조조그린_토큰);

        // then
        ValidAuthResponse actual = toResponseDto(response, ValidAuthResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getIsValid()).isTrue()
        );
    }
}
