package com.woowacourse.smody.controller;

import com.woowacourse.smody.controller.openapi.GoogleApi;
import com.woowacourse.smody.dto.LoginRequest;
import com.woowacourse.smody.dto.LoginResponse;
import com.woowacourse.smody.service.OauthService;
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
}
