package com.woowacourse.smody.cycle.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.dto.ProgressResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.cycle.service.CycleApiService;
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
@RequestMapping("/cycles")
@RequiredArgsConstructor
public class CycleController {

    private final CycleApiService cycleApiService;

    @PostMapping
    @RequiredLogin
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload,
                                       @RequestBody CycleRequest cycleRequest) {
        Long cycleId = cycleApiService.create(tokenPayload, cycleRequest);
        return ResponseEntity.created(URI.create("/cycles/" + cycleId)).build();
    }

    @PostMapping("/{cycleId}/progress")
    @RequiredLogin
    public ResponseEntity<ProgressResponse> increase(@LoginMember TokenPayload tokenPayload,
                                                     @ModelAttribute ProgressRequest progressRequest) {
        progressRequest.setProgressTime(LocalDateTime.now());
        ProgressResponse progressResponse = cycleApiService.increaseProgress(tokenPayload, progressRequest);
        return ResponseEntity.ok(progressResponse);
    }

    @GetMapping(value = "/me")
    @RequiredLogin
    public ResponseEntity<List<InProgressCycleResponse>> findAllInProgressByMe(@LoginMember TokenPayload tokenPayload,
                                                                               @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(
                cycleApiService.findInProgressByMe(tokenPayload, LocalDateTime.now(), pagingParams));
    }

    @GetMapping("/{cycleId}")
    public ResponseEntity<CycleResponse> findWithSuccessCountById(@PathVariable Long cycleId) {
        return ResponseEntity.ok(cycleApiService.findWithSuccessCountById(cycleId));
    }

    @GetMapping("/me/stat")
    @RequiredLogin
    public ResponseEntity<StatResponse> searchStat(@LoginMember TokenPayload tokenPayload) {
        return ResponseEntity.ok(cycleApiService.searchStat(tokenPayload));
    }

    @GetMapping("/me/{challengeId}")
    @RequiredLogin
    public ResponseEntity<List<FilteredCycleHistoryResponse>> findAllByMemberAndChallengeWithFilter(
            @LoginMember TokenPayload tokenPayload,
            @PathVariable Long challengeId,
            @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(
                cycleApiService.findAllByMemberAndChallenge(tokenPayload, challengeId, pagingParams));
    }
}

