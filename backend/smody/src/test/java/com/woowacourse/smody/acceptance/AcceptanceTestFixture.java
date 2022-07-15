package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.dto.CycleRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AcceptanceTestFixture {

    public static ExtractableResponse<Response> 사이클_생성_요청(String token, LocalDateTime startTime, Long challengeId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(new CycleRequest(startTime, challengeId))
                .when()
                .post("/cycles")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사이클_조회_요청(String cycleId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cycles/" + cycleId)
                .then().log().all()
                .extract();
    }
}
