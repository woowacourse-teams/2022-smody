package com.woowacourse.smody.member.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @RequiredLogin
    public ResponseEntity<MemberResponse> searchMyInfo(@LoginMember TokenPayload tokenPayload) {
        MemberResponse memberResponse = memberService.searchMyInfo(tokenPayload);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @RequiredLogin
    public ResponseEntity<Void> updateMyInfo(@LoginMember TokenPayload tokenPayload,
                                             @RequestBody MemberUpdateRequest updateRequest) {
        memberService.updateMyInfo(tokenPayload, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/profile-image")
    @RequiredLogin
    public ResponseEntity<Void> updateMyProfileImage(@LoginMember TokenPayload tokenPayload,
                                                     @RequestPart MultipartFile profileImage) {
        memberService.updateProfileImage(tokenPayload, profileImage);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @RequiredLogin
    public ResponseEntity<Void> withdraw(@LoginMember TokenPayload tokenPayload) {
        memberService.withdraw(tokenPayload);
        return ResponseEntity.noContent().build();
    }
}