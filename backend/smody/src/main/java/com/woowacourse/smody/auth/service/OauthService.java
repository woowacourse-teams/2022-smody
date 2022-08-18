package com.woowacourse.smody.auth.service;

import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.dto.LoginResponse;
import com.woowacourse.smody.auth.dto.PreTokenPayLoad;
import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.dto.ValidAuthResponse;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        return memberRepository.findByEmail(loginRequest.getEmail())
                .map(member -> new LoginResponse(createToken(member), false))
                .orElseGet(() -> new LoginResponse(
                        createToken(memberRepository.save(loginRequest.toMember())), true)
                );
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createToken(new TokenPayload(member.getId()));
    }


    public ValidAuthResponse isValidAuth(PreTokenPayLoad preTokenPayLoad) {
        return new ValidAuthResponse(jwtTokenProvider.validateToken(preTokenPayLoad.getToken()));
    }
}
