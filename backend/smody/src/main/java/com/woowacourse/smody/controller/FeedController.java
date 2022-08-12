package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.FeedRequest;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.service.FeedQueryService;
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

    private final FeedQueryService feedQueryService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> findAll(@ModelAttribute FeedRequest feedRequest) {
        List<FeedResponse> feedResponses = feedQueryService.findAll(feedRequest);
        return ResponseEntity.ok(feedResponses);
    }

    @GetMapping("/{cycleDetailId}")
    public ResponseEntity<FeedResponse> findById(@PathVariable Long cycleDetailId) {
        FeedResponse feedResponse = feedQueryService.searchById(cycleDetailId);
        return ResponseEntity.ok(feedResponse);
    }
}