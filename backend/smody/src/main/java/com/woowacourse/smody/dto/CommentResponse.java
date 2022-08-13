package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponse {

    private Long memberId;
    private String nickname;
    private String picture;
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isMyComment;

    public CommentResponse(Comment comment, Boolean isMyComment) {
        this.memberId = comment.getMember().getId();
        this.nickname = comment.getMember().getNickname();
        this.picture = comment.getMember().getPicture();
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isMyComment = isMyComment;
    }
}
