package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.CycleDetail;
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
    private String description;
    private LocalDateTime progressTime;
    private Long challengeId;
    private String challengeName;
    private Integer commentCount;

    public FeedResponse(CycleDetail cycleDetail) {
        this.cycleDetailId = cycleDetail.getId();
        this.memberId = cycleDetail.getCycle().getMember().getId();
        this.picture = cycleDetail.getCycle().getMember().getPicture();
        this.nickname = cycleDetail.getCycle().getMember().getNickname();
        this.progressImage = cycleDetail.getProgressImage();
        this.description = cycleDetail.getDescription();
        this.progressTime = cycleDetail.getProgressTime();
        this.challengeId = cycleDetail.getCycle().getChallenge().getId();
        this.challengeName = cycleDetail.getCycle().getChallenge().getName();
        this.commentCount = 0;
    }
}
