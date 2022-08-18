package com.woowacourse.smody.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResponse {

    private String accessToken;
    private Boolean isNewMember;
}
