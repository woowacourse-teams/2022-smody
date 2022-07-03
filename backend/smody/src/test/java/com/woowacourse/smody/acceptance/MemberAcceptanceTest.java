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

    @ParameterizedTest(name = "유효하지 않은 password - {0}")
    @ValueSource(strings = {
            "12345678a", "12345678901234567890a",
            "qwertyuiop", "1234567890",
            "12345678 a", " 12345678a",
            "12345678a ", "!12345678a",
            "가12345678a", "12345678a가"})
    void 유효하지_않은_비밀번호는_회원가입을_할_수_없다(String invalidPassword) {
        // when
        ExtractableResponse<Response> response = 회원가입(EMAIL, invalidPassword, NICKNAME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(1005),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("유효하지 않은 비밀번호입니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    @ParameterizedTest(name = "유효하지 않은 nickname - {0}")
    @ValueSource(strings = {"알", "12345678901", " 알파", "파 알", "알파쿤 "})
    void 유효하지_않은_닉네임은_회원가입을_할_수_없다(String invalidNickname) {
        // when
        ExtractableResponse<Response> response = 회원가입(EMAIL, PASSWORD, invalidNickname);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(1004),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("유효하지 않은 닉네임입니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }
}
