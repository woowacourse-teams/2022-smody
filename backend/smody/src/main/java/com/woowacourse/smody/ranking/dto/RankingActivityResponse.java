package com.woowacourse.smody.ranking.dto;

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
}
