package com.woowacourse.smody.member.dto;

import com.woowacourse.smody.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberResponse {

    private String email;
    private String nickname;
    private String picture;
    private String introduction;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
        this.introduction = member.getIntroduction();
    }
}
