package com.woowacourse.smody.ranking.dto;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingActivityResponse {

    private Integer ranking;
    private Long memberId;
    private String nickname;
    private String introduction;
    private String picture;
    private Integer point;

    public RankingActivityResponse(Integer ranking, RankingActivity rankingActivity) {
        this.ranking = ranking;
        Member member = rankingActivity.getMember();
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.introduction = member.getIntroduction();
        this.picture = member.getPicture();
        this.point = rankingActivity.getPoint();
    }
}
