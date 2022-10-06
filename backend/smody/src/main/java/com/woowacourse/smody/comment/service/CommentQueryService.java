package com.woowacourse.smody.comment.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public List<CommentResponse> findAllByCycleDetailId(TokenPayload tokenPayload, Long cycleDetailId) {
        List<Comment> comments = commentRepository.findAllByCycleDetailId(cycleDetailId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment, comment.isCommentByMemberId(tokenPayload.getId())))
                .collect(Collectors.toList());
    }
}
