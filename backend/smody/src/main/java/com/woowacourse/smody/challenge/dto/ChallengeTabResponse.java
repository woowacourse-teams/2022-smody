package com.woowacourse.smody.challenge.dto;

import com.woowacourse.smody.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeTabResponse {

    private Long challengeId;
    private String challengeName;
    private Integer challengerCount;
    private Boolean isInProgress;
    private Integer emojiIndex;
    private Integer colorIndex;

    public ChallengeTabResponse(Challenge challenge, Integer challengerCount, Boolean isInProgress) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengerCount = challengerCount;
        this.isInProgress = isInProgress;
        this.emojiIndex = challenge.getEmojiIndex();
        this.colorIndex = challenge.getColorIndex();
    }

    public ChallengeTabResponse(Challenge challenge, Integer challengerCount) {
        this(challenge, challengerCount, false);
    }

    public ChallengeTabResponse changeInProgress(Boolean inProgress) {
        return new ChallengeTabResponse(challengeId, challengeName, challengerCount, inProgress, emojiIndex,
                colorIndex);
    }
}
