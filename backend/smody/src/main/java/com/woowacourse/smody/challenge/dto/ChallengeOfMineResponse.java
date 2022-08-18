package com.woowacourse.smody.challenge.dto;

import com.woowacourse.smody.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeOfMineResponse {
    private Long challengeId;
    private String challengeName;
    private Integer successCount;
    private Integer emojiIndex;
    private Integer colorIndex;

    public ChallengeOfMineResponse(Challenge challenge, Integer successCount) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.successCount = successCount;
        this.emojiIndex = challenge.getEmojiIndex();
        this.colorIndex = challenge.getColorIndex();
    }
}
