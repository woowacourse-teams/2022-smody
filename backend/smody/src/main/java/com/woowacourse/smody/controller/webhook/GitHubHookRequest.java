package com.woowacourse.smody.controller.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GitHubHookRequest {

    private String ref;

    public String parseBranch() {
        return ref.split("/")[2];
    }
}
