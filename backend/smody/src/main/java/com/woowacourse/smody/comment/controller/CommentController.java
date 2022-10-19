package com.woowacourse.smody.comment.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.service.CommentApiService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentApiService commentApiService;

    @PostMapping("/feeds/{cycleDetailId}/comments")
    @RequiredLogin
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload,
                                       @PathVariable Long cycleDetailId,
                                       @RequestBody CommentRequest commentRequest) {
        Long commentId = commentApiService.create(tokenPayload, cycleDetailId, commentRequest);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @PatchMapping("/comments/{commentId}")
    @RequiredLogin
    public ResponseEntity<Void> update(@LoginMember TokenPayload tokenPayload,
                                       @PathVariable Long commentId,
                                       @RequestBody CommentUpdateRequest commentUpdateRequest) {
        commentApiService.update(tokenPayload, commentId, commentUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{commentId}")
    @RequiredLogin
    public ResponseEntity<Void> delete(@LoginMember TokenPayload tokenPayload, @PathVariable Long commentId) {
        commentApiService.delete(tokenPayload, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feeds/{cycleDetailId}/comments")
    public ResponseEntity<List<CommentResponse>> findAllByCycleDetailId(@PathVariable Long cycleDetailId) {
        return ResponseEntity.ok(
                commentApiService.findAllByCycleDetailId(TokenPayload.NOT_LOGIN_TOKEN_PAYLOAD, cycleDetailId)
        );
    }

    @GetMapping("/feeds/{cycleDetailId}/comments/auth")
    @RequiredLogin
    public ResponseEntity<List<CommentResponse>> findAllByCycleDetailId(@LoginMember TokenPayload tokenPayload,
                                                                        @PathVariable Long cycleDetailId) {
        return ResponseEntity.ok(commentApiService.findAllByCycleDetailId(tokenPayload, cycleDetailId));
    }
}
