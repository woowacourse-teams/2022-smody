package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengersResponse {

    private Long memberId;
    private String nickName;
    private Integer progressCount;
    private String picture;
    private String introduction;

    public ChallengersResponse(Member member, Integer progressCount) {
        this.memberId = member.getId();
        this.nickName = member.getNickname();
        this.progressCount = progressCount;
        this.picture = member.getPicture();
        this.introduction = member.getIntroduction();
    }
}
