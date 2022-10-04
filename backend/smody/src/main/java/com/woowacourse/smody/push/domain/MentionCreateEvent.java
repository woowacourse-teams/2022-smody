package com.woowacourse.smody.push.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MentionCreateEvent {

    private final List<Long> mentionedIds;

    private final Long cycleDetailId;

    private final Long mentioningIds;
}
