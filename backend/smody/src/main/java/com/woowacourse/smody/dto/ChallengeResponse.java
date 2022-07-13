package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Challenge;
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

    public ChallengeResponse(Challenge challenge, Integer challengerCount) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengerCount = challengerCount;
    }
}
