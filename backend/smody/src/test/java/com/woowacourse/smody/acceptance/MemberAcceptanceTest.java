package com.woowacourse.smody.acceptance;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인을_한다() {
        RestAssured.given().log().all()
                .params(Map.of("code", "tonic"))
                .when().get("/login/google")
                .then().log().all()
                .extract();
    }
}
