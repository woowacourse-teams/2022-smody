package com.woowacourse.smody.controller.openapi;

import com.woowacourse.smody.dto.GoogleTokenRequest;
import com.woowacourse.smody.dto.GoogleTokenResponse;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.Base64;
import java.util.Objects;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleApi {

    private static final String GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:3000/home";
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.client-id}")
    private String CLIENT_ID;
    @Value("${oauth.google.client-secret}")
    private String CLIENT_SECRET;

    public String generateLoginLink() {
        return GOOGLE_LOGIN_URL + "?"
                + "client_id=" + CLIENT_ID + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.profile"
                + " https://www.googleapis.com/auth/userinfo.email";
    }

    public LoginRequest requestToken(String authorizationCode) {
        validateAuthorizationCode(authorizationCode);
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest(
                authorizationCode, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE
        );
        GoogleTokenResponse googleTokenResponse = new RestTemplate().postForObject(
                GOOGLE_TOKEN_REQUEST_URI,
                googleTokenRequest,
                GoogleTokenResponse.class
        );
        return parseMemberInfo(googleTokenResponse);
    }

    private void validateAuthorizationCode(String authorizationCode) {
        if (Objects.isNull(authorizationCode)) {
            throw new BusinessException(ExceptionData.INVALID_AUTHORIZATION_CODE);
        }
    }

    private LoginRequest parseMemberInfo(final GoogleTokenResponse googleTokenResponse) {
        byte[] tokenPayload = Base64.getDecoder()
                .decode(googleTokenResponse.getId_token().split("\\.")[1]);
        JSONObject jsonObject = new JSONObject(new String(tokenPayload));
        return new LoginRequest(jsonObject);
    }
}
