package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SmodyTestEnvironmentExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private ImageStrategy imageStrategy;

    protected String 조조그린_토큰;
    protected String 더즈_토큰;
    protected String 토닉_토큰;
    protected String 알파_토큰;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;

        조조그린_토큰 = 토큰_요청(조조그린_ID);
        더즈_토큰 = 토큰_요청(더즈_ID);
        토닉_토큰 = 토큰_요청(토닉_ID);
        알파_토큰 = 토큰_요청(알파_ID);

        given(imageStrategy.extractUrl(any()))
            .willReturn("imageUrl");
    }

    private String 토큰_요청(Long memberId) {
        return tokenProvider.createToken(new TokenPayload(memberId));
    }

    protected <T> List<T> toResponseDtoList(ExtractableResponse<Response> response, Class<T> type) {
        return response.jsonPath().getList(".", type);
    }

    protected <T> T toResponseDto(ExtractableResponse<Response> response, Class<T> type) {
        return response.jsonPath().getObject(".", type);
    }

    protected Long ID_추출(ExtractableResponse<Response> response) {
        return Long.parseLong(
            response.header("location")
                .split("/")[2]
        );
    }

    protected Executable OK_응답(ExtractableResponse<Response> response) {
        return () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    protected Executable CREATED_응답(ExtractableResponse<Response> response) {
        return () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    protected Executable NO_CONTENT_응답(ExtractableResponse<Response> response) {
        return () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
