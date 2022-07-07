package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeResponse {

    private Long challengeId;
    private String challengeName;
    private Integer challengerCount;
}
