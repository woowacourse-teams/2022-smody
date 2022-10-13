package com.woowacourse.smody.auth.controller;

import com.woowacourse.smody.auth.service.GoogleApiService;
import com.woowacourse.smody.auth.dto.LoginRequest;
import com.woowacourse.smody.auth.dto.LoginResponse;
import com.woowacourse.smody.auth.dto.PreTokenPayLoad;
import com.woowacourse.smody.auth.dto.ValidAuthResponse;
import com.woowacourse.smody.auth.service.OauthApiService;
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

    private final OauthApiService oauthApiService;

    private final GoogleApiService googleApiService;

    @GetMapping("/link/google")
    public ResponseEntity<String> linkGoogle() {
        return ResponseEntity.ok(googleApiService.generateLoginLink());
    }

    @GetMapping("/login/google")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestParam String code) {
        LoginRequest loginRequest = googleApiService.requestToken(code);
        return ResponseEntity.ok(oauthApiService.login(loginRequest));
    }

    @GetMapping("/check")
    public ResponseEntity<ValidAuthResponse> isValid(@TokenChecker PreTokenPayLoad preTokenPayLoad) {
        return ResponseEntity.ok(oauthApiService.isValidAuth(preTokenPayLoad));
    }
}
