package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class FeedAcceptanceTest extends AcceptanceTest {

    @DisplayName("피드 전체 조회")
    @Test
    void getFeeds() {
        // given


        // when
        ExtractableResponse<Response> response = 피드_조회_요청(10, null);

        // then
        assertAll(
                OK_응답(response)
        );
    }
}
