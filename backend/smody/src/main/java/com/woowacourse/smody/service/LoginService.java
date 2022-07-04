package com.woowacourse.smody.service;

import com.woowacourse.smody.auth.JwtTokenProvider;
import com.woowacourse.smody.domain.Email;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.MemberRepository;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(new Email(loginRequest.getEmail()))
                .orElseThrow(() -> new BusinessException(ExceptionData.INVALID_LOGIN));
        checkPassword(loginRequest.getPassword(), member);
        return new LoginResponse(member.getNickname().getValue(), createToken(member));
    }

    private void checkPassword(String password, Member member) {
        if (!member.matchPassword(password)) {
            throw new BusinessException(ExceptionData.INVALID_LOGIN);
        }
    }

    private String createToken(Member member) {
        return jwtTokenProvider.createToken(Map.of(
                "id", member.getId(),
                "nickname", member.getNickname().getValue()
        ));
    }
}
