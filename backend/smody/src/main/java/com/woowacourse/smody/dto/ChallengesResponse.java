package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengesResponse {

    private Long challengeId;
    private String challengeName;
    private Integer challengerCount;
    private Boolean isInProgress;
    private Integer emojiIndex;
    private Integer colorIndex;

    public ChallengesResponse(Challenge challenge, Integer challengerCount, Boolean isInProgress) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengerCount = challengerCount;
        this.isInProgress = isInProgress;
        this.emojiIndex = challenge.getEmojiIndex();
    }

    public ChallengesResponse(Challenge challenge, Integer challengerCount) {
        this(challenge, challengerCount, false);
    }

    public ChallengesResponse changeInProgress(Boolean inProgress) {
        return new ChallengesResponse(challengeId, challengeName, challengerCount, inProgress, emojiIndex, colorIndex);
    }
}
