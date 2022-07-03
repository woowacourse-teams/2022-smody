package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.dto.SignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AcceptanceTestFixture {

    public static ExtractableResponse<Response> 회원가입(String email, String password, String nickname) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SignUpRequest(email, password, nickname))
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 이메일_중복검사(String email) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("email", email))
                .when().post("/members/emails/checkDuplicate")
                .then().log().all()
                .extract();
    }
}
