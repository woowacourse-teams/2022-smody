package com.woowacourse.smody.comment.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentApiService {

    private final CommentService commentService;

    public List<CommentResponse> findAllByCycleDetailId(TokenPayload tokenPayload, Long cycleDetailId) {
        List<Comment> comments = commentService.findAllByCycleDetailId(cycleDetailId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment, comment.isCommentByMemberId(tokenPayload.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long create(TokenPayload tokenPayload, Long cycleDetailId, CommentRequest commentRequest) {
        Comment comment = commentService.create(
                tokenPayload.getId(), cycleDetailId, commentRequest.getContent()
        );
        return comment.getId();
    }

    @Transactional
    public void update(
            TokenPayload tokenPayload, Long commentId, CommentUpdateRequest commentUpdateRequest
    ) {
        commentService.update(tokenPayload.getId(), commentId, commentUpdateRequest.getContent());
    }

    @Transactional
    public void delete(TokenPayload tokenPayload, Long commentId) {
        commentService.delete(tokenPayload.getId(), commentId);
    }
}