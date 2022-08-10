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
    private String description;
    private Integer emoji;
    private Integer color;

    public ChallengeResponse(Challenge challenge, Integer challengerCount, Boolean isInProgress) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengerCount = challengerCount;
        this.isInProgress = isInProgress;
        this.emoji = challenge.getEmoji();
        this.color = challenge.getColor();
        this.description = challenge.getDescription();
    }

    public ChallengeResponse(Challenge challenge, Integer challengerCount) {
        this(challenge, challengerCount, false);
    }
}
