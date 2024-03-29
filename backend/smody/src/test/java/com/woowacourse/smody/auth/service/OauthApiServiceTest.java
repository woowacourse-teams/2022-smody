package com.woowacourse.smody.auth.service;

import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.dto.LoginResponse;
import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.repository.MemberRepository;
import com.woowacourse.smody.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OauthApiServiceTest extends IntegrationTest {

    @Autowired
    private OauthApiService oauthApiService;

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
        LoginResponse loginResponse = oauthApiService.login(new LoginRequest(member));
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
        LoginResponse loginResponse = oauthApiService.login(
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
