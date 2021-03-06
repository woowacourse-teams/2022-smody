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
    private Boolean isInProgress;

    public ChallengeResponse(Challenge challenge, Integer challengerCount, Boolean isInProgress) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengerCount = challengerCount;
        this.isInProgress = isInProgress;
    }

    public ChallengeResponse(Challenge challenge, Integer challengerCount) {
        this(challenge, challengerCount, false);
    }

    public ChallengeResponse changeInProgress(Boolean inProgress) {
        return new ChallengeResponse(challengeId, challengeName, challengerCount, inProgress);
    }
}
