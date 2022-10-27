package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class CycleAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인된_회원이_사이클을_생성한다() {
        // given
        String token = 토큰_요청(조조그린_ID);

        // when
        ExtractableResponse<Response> response = 사이클_생성_요청(token, LocalDateTime.now(), 미라클_모닝_ID);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }
}
