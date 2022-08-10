package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Challenge;
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
    private Integer emoji;
    private Integer color;

    public SuccessChallengeResponse(Challenge challenge, Integer successCount) {
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.successCount = successCount;
        this.emoji = challenge.getEmoji();
        this.color = challenge.getColor();
    }
}
