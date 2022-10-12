package com.woowacourse.smody.cycle.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.cycle.dto.*;
import com.woowacourse.smody.cycle.service.CycleQueryService;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.db_support.PagingParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cycles")
@RequiredArgsConstructor
public class CycleController {

    private final CycleService cycleService;
    private final CycleQueryService cycleQueryService;

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload,
                                       @RequestBody CycleRequest cycleRequest) {
        Long cycleId = cycleService.create(tokenPayload, cycleRequest);
        return ResponseEntity.created(URI.create("/cycles/" + cycleId)).build();
    }

    @PostMapping("/{cycleId}/progress")
    @RequiredLogin
    public ResponseEntity<ProgressResponse> increase(@LoginMember TokenPayload tokenPayload,
                                                     @ModelAttribute ProgressRequest progressRequest) {
        progressRequest.setProgressTime(LocalDateTime.now());
        long newId = (long) (Math.random() * 100000) + 1;
        progressRequest.setCycleId(newId);
        ProgressResponse progressResponse = cycleService.increaseProgress(tokenPayload, progressRequest);
        return ResponseEntity.ok(progressResponse);
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<InProgressCycleResponse>> findAllInProgressOfMine(@LoginMember TokenPayload tokenPayload,
                                                                                 @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(
                cycleQueryService.findInProgressOfMine(tokenPayload, LocalDateTime.now(), pagingParams));
    }

    @GetMapping("/{cycleId}")
    public ResponseEntity<CycleResponse> findById(@PathVariable Long cycleId) {
        return ResponseEntity.ok(cycleQueryService.findById(cycleId));
    }

    @GetMapping("/me/stat")
    @RequiredLogin
    public ResponseEntity<StatResponse> searchStat(@LoginMember TokenPayload tokenPayload) {
        return ResponseEntity.ok(cycleQueryService.searchStat(tokenPayload));
    }

    @GetMapping("/me/{challengeId}")
    @RequiredLogin
    public ResponseEntity<List<FilteredCycleHistoryResponse>> findAllByMemberAndChallengeWithFilter(
            @LoginMember TokenPayload tokenPayload,
            @PathVariable Long challengeId,
            @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(
                cycleQueryService.findAllByMemberAndChallenge(tokenPayload, challengeId, pagingParams));
    }
}

