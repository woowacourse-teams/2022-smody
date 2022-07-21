package com.woowacourse.smody.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.auth.JwtTokenExtractor;
import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.controller.openapi.GoogleApi;
import com.woowacourse.smody.service.ChallengeService;
import com.woowacourse.smody.service.CycleService;
import com.woowacourse.smody.service.MemberService;
import com.woowacourse.smody.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
    MemberController.class,
    CycleController.class,
    ChallengeController.class,
    OauthController.class
})
@Import({JwtTokenProvider.class, JwtTokenExtractor.class})
@AutoConfigureRestDocs
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected CycleService cycleService;

    @MockBean
    protected ChallengeService challengeService;

    @MockBean
    protected GoogleApi googleApi;

    @MockBean
    protected OauthService oauthService;
}
