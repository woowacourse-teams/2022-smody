package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.GoogleOauthResponse;
import com.woowacourse.smody.dto.GoogleTokenRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private static final String GOOGLE_LOGIN_LINK = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String CALLBACK_URI = "http://localhost:8080/oauth/callback";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        String location = GOOGLE_LOGIN_LINK + "?"
                + "client_id=" + CLIENT_ID + "&"
                + "redirect_uri=" + CALLBACK_URI + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.profile"
                + " https://www.googleapis.com/auth/userinfo.email";

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", location)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<GoogleOauthResponse> callBack(@RequestParam String code) {
        if (Objects.isNull(code)) {
            throw new IllegalStateException("Google 로그인이 실패했습니다.");
        }
        RestTemplate restTemplate = new RestTemplate();
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest(
                code, CLIENT_ID, CLIENT_SECRET, CALLBACK_URI, "authorization_code"
        );
        GoogleOauthResponse googleOauthResponse =
                restTemplate.postForObject(GOOGLE_TOKEN_REQUEST_URI, googleTokenRequest, GoogleOauthResponse.class);

        // loginResponse 만들기

        return ResponseEntity.ok(googleOauthResponse);
    }
}
