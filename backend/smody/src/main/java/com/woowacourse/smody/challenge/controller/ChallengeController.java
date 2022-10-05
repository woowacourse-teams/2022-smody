package com.woowacourse.smody.challenge.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.challenge.dto.*;
import com.woowacourse.smody.challenge.service.ChallengeQueryService;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.db_support.PagingParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeQueryService challengeQueryService;

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCount(
            @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(
                challengeQueryService.findAllWithChallengerCount(LocalDateTime.now(), pagingParams));
    }

    @GetMapping("/auth")
    @RequiredLogin
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                                 @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(challengeQueryService.findAllWithChallengerCount(
                tokenPayload, LocalDateTime.now(), pagingParams)
        );
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<ChallengeOfMineResponse>> searchOfMineWithFilter(@LoginMember TokenPayload tokenPayload,
                                                                                @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(challengeQueryService.searchOfMine(tokenPayload, pagingParams));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChallengeResponse> findWithChallengerCount(@PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findWithChallengerCount(LocalDateTime.now(), id));
    }

    @GetMapping(value = "/{id}/auth")
    @RequiredLogin
    public ResponseEntity<ChallengeResponse> findWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                     @PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findWithChallengerCount(
                tokenPayload, LocalDateTime.now(), id)
        );
    }

    @GetMapping(value = "/{id}/challengers")
    public ResponseEntity<List<ChallengersResponse>> findAllChallengers(@PathVariable Long id) {
        return ResponseEntity.ok(challengeQueryService.findAllChallengers(id));
    }

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> create(@RequestBody ChallengeRequest challengeRequest) {
        Long challengeId = challengeService.create(challengeRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @GetMapping("/me/{challengeId}")
    @RequiredLogin
    public ResponseEntity<ChallengeHistoryResponse> findWithMine(@LoginMember TokenPayload tokenPayload,
                                                                 @PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeQueryService.findWithMine(tokenPayload, challengeId));
    }
}
