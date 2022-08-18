package com.woowacourse.smody.challenge.dto;

import com.woowacourse.smody.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeHistoryResponse {

    private String challengeName;
    private String description;
    private Integer emojiIndex;
    private Integer colorIndex;
    private Integer successCount;
    private Integer cycleDetailCount;

    public ChallengeHistoryResponse(Challenge challenge, Integer successCount, Integer cycleDetailCount) {
        this.challengeName = challenge.getName();
        this.description = challenge.getDescription();
        this.emojiIndex = challenge.getEmojiIndex();
        this.colorIndex = challenge.getColorIndex();
        this.successCount = successCount;
        this.cycleDetailCount = cycleDetailCount;
    }
}
