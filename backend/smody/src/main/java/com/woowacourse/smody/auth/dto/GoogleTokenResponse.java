package com.woowacourse.smody.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GoogleTokenResponse {

    private String access_token;
    private String scope;
    private String id_token;
    private String token_type;
    private Long expires_in;
}
