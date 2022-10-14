package com.woowacourse.smody.push.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MentionNotificationRequest {

    private List<Long> memberIds;

    private Long pathId;
}
