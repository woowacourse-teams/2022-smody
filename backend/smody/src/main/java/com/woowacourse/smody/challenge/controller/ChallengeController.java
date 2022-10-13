package com.woowacourse.smody.challenge.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeRequest;
import com.woowacourse.smody.challenge.dto.ChallengeResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.challenge.dto.ChallengersResponse;
import com.woowacourse.smody.challenge.service.ChallengeApiService;
import com.woowacourse.smody.db_support.PagingParams;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeApiService challengeApiService;

    @GetMapping
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCountByFilter(
            @ModelAttribute PagingParams pagingParams
    ) {
        return ResponseEntity.ok(
                challengeApiService.findAllWithChallengerCountByFilter(LocalDateTime.now(), pagingParams));
    }

    @GetMapping("/auth")
    @RequiredLogin
    public ResponseEntity<List<ChallengeTabResponse>> findAllWithChallengerCountByFilter(
            @LoginMember TokenPayload tokenPayload,
            @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(challengeApiService.findAllWithChallengerCountByFilter(
                tokenPayload, LocalDateTime.now(), pagingParams)
        );
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<ChallengeOfMineResponse>> findAllByMeAndFilter(@LoginMember TokenPayload tokenPayload,
                                                                              @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(challengeApiService.findAllByMeAndFilter(tokenPayload, pagingParams));
    }

    @GetMapping(value = "/{challengeId}")
    public ResponseEntity<ChallengeResponse> findWithChallengerCount(@PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeApiService.findWithChallengerCount(LocalDateTime.now(), challengeId));
    }

    @GetMapping(value = "/{challengeId}/auth")
    @RequiredLogin
    public ResponseEntity<ChallengeResponse> findWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                     @PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeApiService.findWithChallengerCount(
                tokenPayload, LocalDateTime.now(), challengeId)
        );
    }

    @GetMapping(value = "/{challengeId}/challengers")
    public ResponseEntity<List<ChallengersResponse>> findAllChallengers(@PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeApiService.findAllChallengers(challengeId));
    }

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> create(@RequestBody ChallengeRequest challengeRequest) {
        Long challengeId = challengeApiService.create(challengeRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @GetMapping("/me/{challengeId}")
    @RequiredLogin
    public ResponseEntity<ChallengeHistoryResponse> findByMeAndChallenge(@LoginMember TokenPayload tokenPayload,
                                                                         @PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeApiService.findByMeAndChallenge(tokenPayload, challengeId));
    }
}
