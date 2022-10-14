package com.woowacourse.smody.auth.api;

import com.woowacourse.smody.auth.dto.GoogleTokenRequest;
import com.woowacourse.smody.auth.dto.GoogleTokenResponse;
import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleApi {

    private static final String GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.client-id}")
    private String clientId;
    @Value("${oauth.google.client-secret}")
    private String clientSecret;
    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    public String generateLoginLink() {
        return GOOGLE_LOGIN_URL + "?"
                + "client_id=" + clientId + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.profile"
                + " https://www.googleapis.com/auth/userinfo.email" + "&"
                + "redirect_uri=" + redirectUri;
    }

    public LoginRequest requestToken(String authorizationCode) {
        validateAuthorizationCode(authorizationCode);
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest(
                authorizationCode, clientId, clientSecret, redirectUri, GRANT_TYPE
        );
        GoogleTokenResponse googleTokenResponse = restTemplate.postForObject(
                GOOGLE_TOKEN_REQUEST_URI,
                googleTokenRequest,
                GoogleTokenResponse.class
        );
        validateGoogleTokenResponseNotNull(googleTokenResponse);
        return parseMemberInfo(googleTokenResponse);
    }

    private void validateAuthorizationCode(String authorizationCode) {
        if (Objects.isNull(authorizationCode)) {
            throw new BusinessException(ExceptionData.INVALID_AUTHORIZATION_CODE);
        }
    }

    private void validateGoogleTokenResponseNotNull(GoogleTokenResponse googleTokenResponse) {
        if (googleTokenResponse == null) {
            throw new BusinessException(ExceptionData.GOOGLE_RESPONSE_ERROR);
        }
    }

    private LoginRequest parseMemberInfo(final GoogleTokenResponse googleTokenResponse) {
        byte[] tokenPayload = Base64.getUrlDecoder()
                .decode(googleTokenResponse.getId_token().split("\\.")[1]);
        JSONObject jsonObject = new JSONObject(new String(tokenPayload));
        return new LoginRequest(jsonObject);
    }
}
