package com.woowacourse.smody.dto;

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
}
