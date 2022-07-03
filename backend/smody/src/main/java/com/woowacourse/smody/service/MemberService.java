package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Email;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.EmailRequest;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Member member = memberRepository.save(signUpRequest.toMember());
        return new SignUpResponse(member);
    }

    public void checkDuplicatedEmail(EmailRequest emailRequest) {
        if (memberRepository.existsByEmail(new Email(emailRequest.getEmail()))) {
            throw new BusinessException(ExceptionData.DUPLICATED_EMAIL);
        }
    }
}
