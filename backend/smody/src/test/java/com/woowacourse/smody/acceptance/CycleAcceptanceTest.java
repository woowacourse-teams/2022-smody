package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.로그인;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.사이클_생성;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class CycleAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Test
    void 로그인된_회원이_사이클을_생성한다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);
        String token = 로그인(EMAIL, PASSWORD)
                .as(LoginResponse.class)
                .getAccessToken();

        // when
        ExtractableResponse<Response> response = 사이클_생성(token, LocalDateTime.now(), 1L);
        // TODO
//        ExtractableResponse<Response> cycleResponse = 사이클_조회(response.header("Location").split("/")[2]);
//        Long cycleId = cycleResponse.as(CycleResponse.class).getId();
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/")[2]).isEqualTo(String.valueOf(1L))
        );
    }
}
