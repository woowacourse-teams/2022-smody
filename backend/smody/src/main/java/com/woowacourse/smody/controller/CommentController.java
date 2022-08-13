package com.woowacourse.smody.controller;

import com.woowacourse.smody.dto.CommentResponse;
import com.woowacourse.smody.service.CommentQueryService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentQueryService commentQueryService;

    @PostMapping("/feeds/{cycleDetailId}/comments")
    public ResponseEntity<Void> create(@PathVariable Long cycleDetailId) {
        return ResponseEntity.created(URI.create("/comments/" + 1)).build();
    }

    @GetMapping("/feeds/{cycleDetailId}/comments")
    public ResponseEntity<List<CommentResponse>> findAllByCycleDetailId(@PathVariable Long cycleDetailId) {
        return ResponseEntity.ok(commentQueryService.findAllByFeed(cycleDetailId));
    }
}
