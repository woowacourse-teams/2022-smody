package com.woowacourse.smody.comment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentCreateEvent {

    private final Comment comment;
}
