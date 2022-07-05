package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.EmailRequest;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.NicknameRequest;
import com.woowacourse.smody.dto.SignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
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
                .body(new EmailRequest(email))
                .when().post("/members/emails/checkDuplicate")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 닉네임_중복검사(String nickname) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new NicknameRequest(nickname))
                .when().post("/members/nicknames/checkDuplicate")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사이클_생성(String token, LocalDateTime startTime, Long challengeId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(new CycleRequest(startTime, challengeId))
                .when()
                .post("/cycles")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사이클_조회(String cycleId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cycles/" + cycleId)
                .then().log().all()
                .extract();
    }
}
