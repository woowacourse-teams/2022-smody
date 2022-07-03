package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpResponse {

    private Long id;
    private String email;

    public SignUpResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail().getValue();
    }
}
