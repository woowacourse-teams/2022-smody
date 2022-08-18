package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.사이클_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.member.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CycleAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    @Test
    void 로그인된_회원이_사이클을_생성한다() {
        // given
        String token = 로그인_혹은_회원가입(new Member(EMAIL, NICKNAME, PICTURE));

        // when
        ExtractableResponse<Response> response = 사이클_생성_요청(token, LocalDateTime.now(), 1L);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }
}
