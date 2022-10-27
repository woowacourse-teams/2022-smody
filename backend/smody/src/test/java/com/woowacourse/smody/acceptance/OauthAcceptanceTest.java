package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.token.JwtTokenProvider;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class OauthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 유효한_토큰으로_검증한다() {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(조조그린_ID));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .get("/oauth/check")
                .then().log().all()
                .extract();

        // then
        assertAll(
                OK_응답(response),
                () -> assertThat(response.body().jsonPath().getBoolean("isValid")).isTrue()
        );
    }
}
