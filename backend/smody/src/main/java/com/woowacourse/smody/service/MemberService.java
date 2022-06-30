package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Member member = memberRepository.save(signUpRequest.toMember());
        return new SignUpResponse(member);
    }
}
