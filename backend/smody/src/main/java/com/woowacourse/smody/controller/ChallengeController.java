package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.service.ChallengeQueryService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import com.woowacourse.smody.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeQueryService challengeQueryService;

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCount(Pageable pageable,
                                                                                 @RequestParam(required = false) String search) {
        return ResponseEntity.ok(challengeQueryService.findAllWithChallengerCount(LocalDateTime.now(), pageable, search));
    }

    @GetMapping("/auth")
    @RequiredLogin
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                                 Pageable pageable,
                                                                                 @RequestParam(required = false) String search) {
        return ResponseEntity.ok(challengeQueryService.findAllWithChallengerCount(
                tokenPayload, LocalDateTime.now(), pageable, search)
        );
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<SuccessChallengeResponse>> searchSuccessOfMine(@LoginMember TokenPayload tokenPayload,
                                                                              Pageable pageable) {
        return ResponseEntity.ok(challengeQueryService.searchSuccessOfMine(tokenPayload, pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChallengeResponse> findOneWithChallengerCount(@PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findOneWithChallengerCount(LocalDateTime.now(), id));
    }

    @GetMapping(value = "/{id}/auth")
    @RequiredLogin
    public ResponseEntity<ChallengeResponse> findOneWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                        @PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findOneWithChallengerCount(
                tokenPayload, LocalDateTime.now(), id)
        );
    }

    @GetMapping(value = "/{id}/challengers")
    public ResponseEntity<List<ChallengersResponse>> findAllChallengers(@PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findAllChallengers(id));
    }

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> create(ChallengeRequest challengeRequest) {
        Long challengeId = challengeService.create(challengeRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @GetMapping("/searched")
    public ResponseEntity<List<ChallengesResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(challengeQueryService.searchByName(name));
    }

    @GetMapping("/searched/auth")
    @RequiredLogin
    public ResponseEntity<List<ChallengesResponse>> searchByName(@LoginMember TokenPayload tokenPayload,
                                                                 @RequestParam String name) {
        return ResponseEntity.ok(challengeQueryService.searchByName(tokenPayload, name));
    }
}
