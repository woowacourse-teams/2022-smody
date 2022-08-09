package com.woowacourse.smody.dto;

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
}
