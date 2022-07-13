package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.사이클_생성;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.LoginResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CycleAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    // TODO
//    @Test
//    void 로그인된_회원이_사이클을_생성한다() {
//        // given
//        회원가입(EMAIL, PASSWORD, NICKNAME);
//        String token = 로그인(EMAIL, PASSWORD)
//                .as(LoginResponse.class)
//                .getAccessToken();
//
//        // when
//        ExtractableResponse<Response> response = 사이클_생성(token, LocalDateTime.now(), 1L);
//
//        // then
//        assertAll(
//                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
//                () -> assertThat(response.header("Location")).isNotNull()
//        );
//    }
}
