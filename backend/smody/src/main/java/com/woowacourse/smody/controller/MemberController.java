package com.woowacourse.smody.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.MemberUpdateRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> searchMyInfo(@LoginMember TokenPayload tokenPayload) {
        MemberResponse memberResponse = memberService.searchMyInfo(tokenPayload);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyInfo(@LoginMember TokenPayload tokenPayload,
                                             @RequestBody MemberUpdateRequest updateRequest) {
        memberService.updateMyInfo(tokenPayload, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(@LoginMember TokenPayload tokenPayload) {
        memberService.withdraw(tokenPayload);
        return ResponseEntity.noContent().build();
    }
}
