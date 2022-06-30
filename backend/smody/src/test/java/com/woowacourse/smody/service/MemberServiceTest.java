package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void signUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("alpha@naver.com", "abcde12345", "손수건");

        // when
        SignUpResponse signUpResponse = memberService.signUp(signUpRequest);

        // then
        assertAll(
                () -> assertThat(signUpResponse.getId()).isNotNull(),
                () -> assertThat(signUpResponse.getEmail()).isEqualTo(signUpRequest.getEmail())
        );
    }
}
