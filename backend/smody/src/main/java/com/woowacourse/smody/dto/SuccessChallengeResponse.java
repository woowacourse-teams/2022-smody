package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SuccessChallengeResponse {
    private Long challengeId;
    private String challengeName;
    private Integer successCount;
}
