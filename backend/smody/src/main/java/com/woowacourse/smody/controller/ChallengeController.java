package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.service.ChallengeQueryService;
import com.woowacourse.smody.service.ChallengeService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeQueryService challengeQueryService;

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCount(Pageable pageable,
                                                                                 @RequestParam(required = false) String search) {
        return ResponseEntity.ok(
                challengeQueryService.findAllWithChallengerCount(LocalDateTime.now(), pageable, search));
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
    public ResponseEntity<List<ChallengeOfMineResponse>> searchOfMineWithFilter(@LoginMember TokenPayload tokenPayload,
                                                                                @ModelAttribute ChallengeOfMineRequest challengeOfMineRequest) {
        return ResponseEntity.ok(challengeQueryService.searchOfMineWithFilter(tokenPayload, challengeOfMineRequest));
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
