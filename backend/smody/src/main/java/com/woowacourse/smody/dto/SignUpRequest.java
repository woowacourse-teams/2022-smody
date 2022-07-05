package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpRequest {

    private String email;
    private String password;
    private String nickname;

    public Member toMember() {
        return new Member(email, password, nickname);
    }
}
