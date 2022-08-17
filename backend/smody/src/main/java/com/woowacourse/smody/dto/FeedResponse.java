package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime progressTime;
    private Long challengeId;
    private String challengeName;
    private Integer commentCount;

    public FeedResponse(Feed feed) {
        this.cycleDetailId = feed.getCycleDetailId();
        this.memberId = feed.getMemberId();
        this.picture = feed.getPicture();
        this.nickname = feed.getNickname();
        this.progressImage = feed.getProgressImage();
        this.description = feed.getDescription();
        this.progressTime = feed.getProgressTime();
        this.challengeId = feed.getChallengeId();
        this.challengeName = feed.getChallengeName();
        this.commentCount = feed.getCommentCount();
    }

    @Override
    public String toString() {
        return "FeedResponse{" +
                "cycleDetailId=" + cycleDetailId +
                ", memberId=" + memberId +
                ", picture='" + picture + '\'' +
                ", nickname='" + nickname + '\'' +
                ", progressImage='" + progressImage + '\'' +
                ", description='" + description + '\'' +
                ", progressTime=" + progressTime +
                ", challengeId=" + challengeId +
                ", challengeName='" + challengeName + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }
}
