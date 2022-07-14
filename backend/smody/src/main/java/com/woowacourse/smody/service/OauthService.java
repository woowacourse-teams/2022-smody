package com.woowacourse.smody.service;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseGet(() -> memberRepository.save(
                        new Member(loginRequest.getEmail(), loginRequest.getName(), loginRequest.getPicture())
                ));
        return new LoginResponse(createToken(member));
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createToken(new TokenPayload(member.getId()));
    }
}
