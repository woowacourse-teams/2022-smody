package com.woowacourse.smody.push.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MentionNotificationRequest {

    private List<Long> memberIds;

    private Long pathId;
}
