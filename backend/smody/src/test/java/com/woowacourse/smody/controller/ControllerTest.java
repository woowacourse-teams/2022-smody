package com.woowacourse.smody.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.service.LoginService;
import com.woowacourse.smody.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({MemberController.class, LoginController.class})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LoginService loginService;

    @MockBean
    protected MemberService memberService;
}
