package com.woowacourse.smody.controller;

import com.woowacourse.smody.domain.PagingParams;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.service.FeedQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedQueryService feedQueryService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> findAll(@ModelAttribute PagingParams pagingParams) {
        List<FeedResponse> feedResponses = feedQueryService.findAll(pagingParams);
        return ResponseEntity.ok(feedResponses);
    }

    @GetMapping("/{cycleDetailId}")
    public ResponseEntity<FeedResponse> findById(@PathVariable Long cycleDetailId) {
        FeedResponse feedResponse = feedQueryService.searchById(cycleDetailId);
        return ResponseEntity.ok(feedResponse);
    }
}
