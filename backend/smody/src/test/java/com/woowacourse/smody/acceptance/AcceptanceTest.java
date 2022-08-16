package com.woowacourse.smody.acceptance;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.service.OauthService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/data.sql")
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "/truncate.sql")
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
