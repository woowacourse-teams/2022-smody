package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FeedResponse {

    private Long cycleDetailId;
    private Long memberId;
    private String picture;
    private String nickname;
    private String progressImage;
    private String description;
    private String progressTime;
    private String challengeName;
    private Integer commentCount;
}
