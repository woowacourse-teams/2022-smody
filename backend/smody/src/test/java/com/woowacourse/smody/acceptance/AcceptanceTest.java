package com.woowacourse.smody.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SmodyTestEnvironmentExtension.class)
class AcceptanceTest {

    @Autowired
    protected JwtTokenProvider tokenProvider;

    @LocalServerPort
    private int port;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

    protected String 토큰_요청(Long memberId) {
        return tokenProvider.createToken(new TokenPayload(memberId));
    }
}
