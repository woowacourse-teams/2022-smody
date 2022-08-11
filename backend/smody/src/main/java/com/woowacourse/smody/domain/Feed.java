package com.woowacourse.smody.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feed {

    private Long cycleDetailId;
    private String progressImage;
    private String description;
    private LocalDateTime progressTime;
    private Long memberId;
    private String picture;
    private String nickname;
    private Long challengeId;
    private String challengeName;

    public Feed(
            Long cycleDetailId, String progressImage, String description, LocalDateTime progressTime,
            Long memberId, String picture, String nickname,
            Long challengeId, String challengeName) {
        this.cycleDetailId = cycleDetailId;
        this.progressImage = progressImage;
        this.description = description;
        this.progressTime = progressTime;
        this.memberId = memberId;
        this.picture = picture;
        this.nickname = nickname;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
    }
}
