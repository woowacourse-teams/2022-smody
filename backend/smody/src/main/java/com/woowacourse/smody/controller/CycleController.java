package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.domain.PagingParams;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.service.CycleQueryService;
import com.woowacourse.smody.service.CycleService;
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
        ProgressResponse progressResponse = cycleService.increaseProgress(tokenPayload, progressRequest);
        return ResponseEntity.ok(progressResponse);
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<InProgressCycleResponse>> findAllInProgressOfMine(@LoginMember TokenPayload tokenPayload,
                                                                                 @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(cycleQueryService.findInProgressOfMine(tokenPayload, LocalDateTime.now(), pagingParams));
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
    public ResponseEntity<List<FilteredCycleHistoryResponse>> findAllByMemberAndChallengeWithFilter(@LoginMember TokenPayload tokenPayload,
                                                                                                    @PathVariable Long challengeId,
                                                                                                    @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(cycleQueryService.findAllByMemberAndChallengeWithFilter(tokenPayload, challengeId, pagingParams));
    }
}

