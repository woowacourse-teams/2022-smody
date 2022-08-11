package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeRequest {

    private String challengeName;
    private String description;
    private Integer emojiIndex;
    private Integer colorIndex;
}
