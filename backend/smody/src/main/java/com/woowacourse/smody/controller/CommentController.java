package com.woowacourse.smody.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @PostMapping("/feeds/{cycleDetailId}/comments")
    public ResponseEntity<Void> create(@PathVariable Long cycleDetailId) {
        return ResponseEntity.created(URI.create("/comments/" + 1)).build();
    }
}
