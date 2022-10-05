package com.woowacourse.smody.feed.controller;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.dto.FeedResponse;
import com.woowacourse.smody.feed.service.FeedQueryService;
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
