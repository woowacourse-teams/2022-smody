package com.woowacourse.smody.feed.dto;

import com.woowacourse.smody.feed.domain.Feed;
import java.time.LocalDateTime;
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
    private Integer progressCount;
    private LocalDateTime progressTime;
    private String description;
    private Long challengeId;
    private String challengeName;
    private Integer commentCount;

    public FeedResponse(Feed feed) {
        this.cycleDetailId = feed.getCycleDetailId();
        this.memberId = feed.getMemberId();
        this.picture = feed.getPicture();
        this.nickname = feed.getNickname();
        this.progressImage = feed.getProgressImage();
        this.progressCount = feed.getProgress().getCount();
        this.description = feed.getDescription();
        this.progressTime = feed.getProgressTime();
        this.challengeId = feed.getChallengeId();
        this.challengeName = feed.getChallengeName();
        this.commentCount = feed.getCommentCount();
    }
}
