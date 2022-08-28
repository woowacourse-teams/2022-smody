package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.service.OauthService;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.support.isoloation.DatabaseCleanerExtension;
import com.woowacourse.smody.support.isoloation.DatabaseInitializerExtension;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith({DatabaseInitializerExtension.class, DatabaseCleanerExtension.class})
class AcceptanceTest {

    @Autowired
    protected OauthService oauthService;

    @LocalServerPort
    private int port;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

    protected String 로그인_혹은_회원가입(Member member) {
        return oauthService.login(new LoginRequest(member))
                .getAccessToken();
    }
}
