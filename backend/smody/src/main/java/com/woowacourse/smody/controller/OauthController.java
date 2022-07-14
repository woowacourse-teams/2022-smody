package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.GoogleTokenRequest;
import com.woowacourse.smody.dto.GoogleTokenResponse;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.service.LoginService;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private static final String GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:3000/home";
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.client-id}")
    private String CLIENT_ID;
    @Value("${oauth.google.client-secret}")
    private String CLIENT_SECRET;

    private final LoginService loginService;

    @GetMapping("/link/google")
    public ResponseEntity<String> login() {
        String googleLoginUri = GOOGLE_LOGIN_URL + "?"
                + "client_id=" + CLIENT_ID + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.profile"
                + " https://www.googleapis.com/auth/userinfo.email";
        return ResponseEntity.ok(googleLoginUri);
    }

    @GetMapping("/login/google")
    public ResponseEntity<LoginResponse> callBack(@RequestParam String code) {
        validateAuthorizationCode(code);
        GoogleTokenResponse googleTokenResponse = requestGoogleToken(code);
        LoginResponse loginResponse = loginService.login(parseMemberInfo(googleTokenResponse));
        return ResponseEntity.ok(loginResponse);
    }

    private GoogleTokenResponse requestGoogleToken(final String code) {
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest(
                code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE
        );
        GoogleTokenResponse googleTokenResponse = new RestTemplate()
                .postForObject(
                        GOOGLE_TOKEN_REQUEST_URI,
                        googleTokenRequest,
                        GoogleTokenResponse.class
                );
        return googleTokenResponse;
    }

    private LoginRequest parseMemberInfo(final GoogleTokenResponse googleTokenResponse) {
        byte[] tokenPayload = Base64.getDecoder()
                .decode(googleTokenResponse.getId_token().split("\\.")[1]);
        JSONObject jsonObject = new JSONObject(new String(tokenPayload));
        LoginRequest loginRequest = new LoginRequest(jsonObject);
        return loginRequest;
    }

    private void validateAuthorizationCode(final String code) {
        if (Objects.isNull(code)) {
            throw new BusinessException(ExceptionData.INVALID_AUTHORIZATION_CODE);
        }
    }
}
