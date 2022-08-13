package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.dto.TokenPayload;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 유효한_토큰으로_검증한다() {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .get("/members/auth")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getBoolean("isValid")).isTrue()
        );
    }
}
