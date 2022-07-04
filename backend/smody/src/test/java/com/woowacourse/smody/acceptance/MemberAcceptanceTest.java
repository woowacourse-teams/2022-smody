package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.닉네임_중복검사;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.로그인;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.이메일_중복검사;
import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.LoginResponse;
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

    @Test
    void 중복되지_않은_이메일임을_알려준다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 이메일_중복검사("does@gmail.com");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 중복된_이메일_임을_알려준다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 이메일_중복검사(EMAIL);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(1001),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("이미 존재하는 이메일입니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    @Test
    void 중복된_닉네임_임을_알려준다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 닉네임_중복검사(NICKNAME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(1002),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("이미 존재하는 닉네임입니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    @Test
    void 로그인을_한다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 로그인(EMAIL, PASSWORD);
        LoginResponse loginResponse = response.as(LoginResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(loginResponse.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(loginResponse.getAccessToken()).isNotNull()
        );
    }

    @Test
    void 비밀번호가_일치하지_않아서_로그인을_할_수_없다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 로그인(EMAIL, "failedPassword0");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(2001),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("이메일 혹은 비밀번호가 일치하지 않습니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @Test
    void 존재하지_않는_이메일로는_로그인을_할_수_없다() {
        // given
        회원가입(EMAIL, PASSWORD, NICKNAME);

        // when
        ExtractableResponse<Response> response = 로그인("notExist@naver.com", PASSWORD);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getInt("code")).isEqualTo(2001),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("이메일 혹은 비밀번호가 일치하지 않습니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }
}
