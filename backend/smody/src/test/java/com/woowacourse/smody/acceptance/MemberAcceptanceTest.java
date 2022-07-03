package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("회원 관련 기능")
class MemberAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Test
    void 회원가입을_성공한다() {
        // when
        ExtractableResponse<Response> response = 회원가입(EMAIL, PASSWORD, NICKNAME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @ParameterizedTest(name = "유효하지 않은 email - {0}")
    @ValueSource(strings = {
            "alpha@navercom", "alpha#naver.com",
            "alpha @naver.com", " alpha@naver.com",
            "alpha@naver.com ", "alpha@na ver.com",
            "alpha@naver .com", "alphanaver.com",
            "@naver.com", "com.naver@alpha",
            " ", ""})
    void 유효하지_않은_이메일로는_회원가입을_할_수_없다(String invalidEmail) {
        // when
        ExtractableResponse<Response> response = 회원가입(invalidEmail, PASSWORD, NICKNAME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(1003),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("유효하지 않은 이메일입니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }
}
