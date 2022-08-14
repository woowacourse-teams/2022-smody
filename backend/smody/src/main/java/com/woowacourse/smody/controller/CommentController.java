package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.auth.RequiredLogin;
import com.woowacourse.smody.dto.CommentRequest;
import com.woowacourse.smody.dto.CommentResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.CommentQueryService;
import com.woowacourse.smody.service.CommentService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentQueryService commentQueryService;
    private final CommentService commentService;

    @PostMapping("/feeds/{cycleDetailId}/comments")
    @RequiredLogin
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload, @PathVariable Long cycleDetailId,
                                       @RequestBody CommentRequest commentRequest) {
        Long commentId = commentService.create(tokenPayload, cycleDetailId, commentRequest);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @GetMapping("/feeds/{cycleDetailId}/comments")
    public ResponseEntity<List<CommentResponse>> findAllByCycleDetailId(@PathVariable Long cycleDetailId) {
        return ResponseEntity.ok(commentQueryService.findAllByCycleDetailId(new TokenPayload(0L), cycleDetailId));
    }

    @GetMapping("/feeds/{cycleDetailId}/comments/auth")
    @RequiredLogin
    public ResponseEntity<List<CommentResponse>> findAllByCycleDetailId(@LoginMember TokenPayload tokenPayload,
                                                                        @PathVariable Long cycleDetailId) {
        return ResponseEntity.ok(commentQueryService.findAllByCycleDetailId(tokenPayload, cycleDetailId));
    }
}
