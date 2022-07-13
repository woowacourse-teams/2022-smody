package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GoogleOauthResponse {

    private String access_token;
    private String scope;
    private String id_token;
    private String token_type;
    private Long expires_in;
}
