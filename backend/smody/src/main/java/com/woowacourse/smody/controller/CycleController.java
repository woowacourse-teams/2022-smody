package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.StatResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.CycleQueryService;
import com.woowacourse.smody.service.CycleService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cycles")
@RequiredArgsConstructor
public class CycleController {

    private final CycleService cycleService;
    private final CycleQueryService cycleQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload,
                                       @RequestBody CycleRequest cycleRequest) {
        Long cycleId = cycleService.create(tokenPayload, cycleRequest);
        return ResponseEntity.created(URI.create("/cycles/" + cycleId)).build();
    }

    @PostMapping("/{cycleId}/progress")
    public ResponseEntity<ProgressResponse> increase(@LoginMember TokenPayload tokenPayload,
                                                     @PathVariable Long cycleId) {
        ProgressRequest progressRequest = new ProgressRequest(cycleId, LocalDateTime.now());
        ProgressResponse progressResponse = cycleService.increaseProgress(tokenPayload, progressRequest);
        return ResponseEntity.ok(progressResponse);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<List<CycleResponse>> findAllInProgressOfMine(@LoginMember TokenPayload tokenPayload,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(cycleQueryService.findInProgressOfMine(tokenPayload, LocalDateTime.now(), pageable));
    }

    @GetMapping("/{cycleId}")
    public ResponseEntity<CycleResponse> findById(@PathVariable Long cycleId) {
        return ResponseEntity.ok(cycleQueryService.findById(cycleId));
    }

    @GetMapping("/me/stat")
    public ResponseEntity<StatResponse> searchStat(@LoginMember TokenPayload tokenPayload) {
        return ResponseEntity.ok(cycleQueryService.searchStat(tokenPayload));
    }
}
