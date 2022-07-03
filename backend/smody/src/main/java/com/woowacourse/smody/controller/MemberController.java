package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.EmailRequest;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.service.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = memberService.signUp(signUpRequest);
        return ResponseEntity.created(URI.create("/members/" + signUpResponse.getId()))
                .body(signUpResponse);
    }

    @PostMapping("/emails/checkDuplicate")
    public ResponseEntity<Void> checkDuplicatedEmail(@RequestBody EmailRequest emailRequest) {
        memberService.checkDuplicatedEmail(emailRequest);
        return ResponseEntity.ok().build();
    }
}
