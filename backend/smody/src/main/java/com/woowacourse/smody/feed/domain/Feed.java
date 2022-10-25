package com.woowacourse.smody.feed.domain;

import com.woowacourse.smody.cycle.domain.Progress;
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
    private Progress progress;
    private Long memberId;
    private String picture;
    private String nickname;
    private Long challengeId;
    private String challengeName;
    private Integer commentCount;

    public Feed(
            Long cycleDetailId, String progressImage, String description,
            LocalDateTime progressTime, Progress progress,
            Long memberId, String picture, String nickname,
            Long challengeId, String challengeName,
            Integer commentCount
    ) {
        this.cycleDetailId = cycleDetailId;
        this.progressImage = progressImage;
        this.description = description;
        this.progress = progress;
        this.progressTime = progressTime;
        this.memberId = memberId;
        this.picture = picture;
        this.nickname = nickname;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.commentCount = commentCount;
    }

    /**
     * QueryDSL Projection 을 위한 생성자
     */
    public Feed(
            Long cycleDetailId, String progressImage, String description,
            LocalDateTime progressTime, Progress progress,
            Long memberId, String picture, String nickname,
            Long challengeId, String challengeName,
            Long commentCount
    ) {
        this(cycleDetailId, progressImage,
                description, progressTime, progress,
                memberId, picture, nickname,
                challengeId, challengeName,
                Math.toIntExact(commentCount)
        );
    }
}
