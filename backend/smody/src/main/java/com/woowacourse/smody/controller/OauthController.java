package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.GoogleOauthResponse;
import com.woowacourse.smody.dto.GoogleTokenRequest;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.service.LoginService;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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

    private static final String CLIENT_ID = "671293991399-in1m6ggna174bmmvjti10rjg4o85o609.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";

    private final LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        String googleLoginUri = GOOGLE_LOGIN_URL + "?"
                + "client_id=" + CLIENT_ID + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.profile"
                + " https://www.googleapis.com/auth/userinfo.email";
        return ResponseEntity.ok(googleLoginUri);
    }

    @GetMapping("/callback")
    public ResponseEntity<LoginResponse> callBack(@RequestParam String code) {
        if (Objects.isNull(code)) {
            throw new IllegalStateException("Google 로그인이 실패했습니다.");
        }

        // access token 구글에 요청
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest(
                code, CLIENT_ID, CLIENT_SECRET, "http://localhost:3000/home", "authorization_code"
        );

        GoogleOauthResponse googleOauthResponse =
                new RestTemplate().postForObject(
                        GOOGLE_TOKEN_REQUEST_URI,
                        googleTokenRequest,
                        GoogleOauthResponse.class
                );

        // 유틸 : payload 복호화
        byte[] tokenPayload = Base64.getDecoder()
                .decode(googleOauthResponse.getId_token().split("\\.")[1]);
        JSONObject jsonObject = new JSONObject(new String(tokenPayload));

        return ResponseEntity.ok(loginService.login(new LoginRequest(jsonObject)));
    }
}
