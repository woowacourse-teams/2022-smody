package com.woowacourse.smody.feed.controller;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.dto.FeedResponse;
import com.woowacourse.smody.feed.service.FeedApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedApiService feedApiService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> findAll(@ModelAttribute PagingParams pagingParams) {
        List<FeedResponse> feedResponses = feedApiService.findAll(pagingParams);
        return ResponseEntity.ok(feedResponses);
    }

    @GetMapping("/{cycleDetailId}")
    public ResponseEntity<FeedResponse> findById(@PathVariable Long cycleDetailId) {
        FeedResponse feedResponse = feedApiService.findById(cycleDetailId);
        return ResponseEntity.ok(feedResponse);
    }
}
