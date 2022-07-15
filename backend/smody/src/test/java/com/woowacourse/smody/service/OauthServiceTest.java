package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OauthServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    @Autowired
    private OauthService oauthService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("이미 등록된 회원이면 로그인에 성공한다.")
    @Test
    void login() {
        // given
        Member member = new Member(EMAIL, NICKNAME, PICTURE);
        memberRepository.save(member);

        // when
        LoginResponse loginResponse = oauthService.login(new LoginRequest(member));
        TokenPayload payload = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        // then
        assertThat(payload.getId()).isEqualTo(member.getId());
    }

    @DisplayName("등록되지 않은 회원이면 회원가입 후 로그인에 성공한다.")
    @Test
    void login_enroll() {
        // given
        Member member = new Member(EMAIL, NICKNAME, PICTURE);

        // when
        LoginResponse loginResponse = oauthService.login(new LoginRequest(member));
        TokenPayload payload = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        // then
        assertThat(payload.getId()).isEqualTo(memberRepository.findByEmail(EMAIL).get().getId());
    }
}
