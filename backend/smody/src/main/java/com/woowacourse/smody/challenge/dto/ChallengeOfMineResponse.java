package com.woowacourse.smody.challenge.dto;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.MemberChallenge;
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

    public ChallengeOfMineResponse(MemberChallenge memberChallenge) {
        Challenge challenge = memberChallenge.getChallenge();
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.successCount = memberChallenge.getSuccessCount();
        this.emojiIndex = challenge.getEmojiIndex();
        this.colorIndex = challenge.getColorIndex();
    }
}
