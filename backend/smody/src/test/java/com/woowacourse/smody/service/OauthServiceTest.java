package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.MemberRepository;

@SpringBootTest
@Transactional
public class OauthServiceTest extends IntegrationTest {

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
        Member member = fixture.회원_조회(조조그린_ID);

        // when
        LoginResponse loginResponse = oauthService.login(new LoginRequest(member));
        TokenPayload payload = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        // then
        assertAll(
                () -> assertThat(payload.getId()).isEqualTo(member.getId()),
                () -> assertThat(loginResponse.getIsNewMember()).isFalse()
        );
    }

    @DisplayName("등록되지 않은 회원이면 회원가입 후 로그인에 성공한다.")
    @Test
    void login_enroll() {
        // when
        String email = "alpha@naver.com";
        LoginResponse loginResponse = oauthService.login(
                new LoginRequest(email, "손수건", "사진")
        );
        TokenPayload payload = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        // then
        assertAll(
                () -> assertThat(payload.getId())
                        .isEqualTo(memberRepository.findByEmail(email).get().getId()),
                () -> assertThat(loginResponse.getIsNewMember())
                        .isTrue()
        );
    }
}
