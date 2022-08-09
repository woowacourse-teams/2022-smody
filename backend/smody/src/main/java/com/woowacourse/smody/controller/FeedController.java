package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.service.FeedQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedQueryService feedQueryService;

    @GetMapping("/feeds")
    ResponseEntity<List<FeedResponse>> findAll(Pageable pageable) {
        List<FeedResponse> feedResponses = feedQueryService.searchAll();
        return ResponseEntity.ok(feedResponses);
    }
}
