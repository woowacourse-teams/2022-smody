package com.woowacourse.smody.member.dto;

import com.woowacourse.smody.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MentionResponse {

    private Long memberId;
    private String nickname;
    private String picture;

    public MentionResponse(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
    }
}
