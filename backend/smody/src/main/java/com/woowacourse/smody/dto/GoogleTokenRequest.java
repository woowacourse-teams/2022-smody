package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GoogleTokenRequest {

    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;
}
