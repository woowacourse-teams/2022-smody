package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.service.OauthApiService;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SmodyTestEnvironmentExtension.class)
class AcceptanceTest {

    @Autowired
    protected OauthApiService oauthApiService;

    @LocalServerPort
    private int port;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

    protected String 로그인_혹은_회원가입(Member member) {
        return oauthApiService.login(new LoginRequest(member))
                .getAccessToken();
    }
}
