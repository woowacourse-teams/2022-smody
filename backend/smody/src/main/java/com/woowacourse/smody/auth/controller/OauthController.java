package com.woowacourse.smody.auth.controller;

import com.woowacourse.smody.auth.api.GoogleApi;
import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.dto.LoginResponse;
import com.woowacourse.smody.auth.dto.PreTokenPayLoad;
import com.woowacourse.smody.auth.dto.ValidAuthResponse;
import com.woowacourse.smody.auth.service.OauthService;
import com.woowacourse.smody.auth.token.TokenChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    private final GoogleApi googleApi;

    @GetMapping("/link/google")
    public ResponseEntity<String> linkGoogle() {
        return ResponseEntity.ok(googleApi.generateLoginLink());
    }

    @GetMapping("/login/google")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestParam String code) {
        LoginRequest loginRequest = googleApi.requestToken(code);
        return ResponseEntity.ok(oauthService.login(loginRequest));
    }

    @GetMapping("/check")
    public ResponseEntity<ValidAuthResponse> isValid(@TokenChecker PreTokenPayLoad preTokenPayLoad) {
        return ResponseEntity.ok(oauthService.isValidAuth(preTokenPayLoad));
    }
}
