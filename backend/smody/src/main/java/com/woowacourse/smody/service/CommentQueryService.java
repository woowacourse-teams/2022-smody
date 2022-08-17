package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.Feed;
import com.woowacourse.smody.dto.CommentResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;
    private final FeedService feedService;

    public List<CommentResponse> findAllByCycleDetailId(TokenPayload tokenPayload, Long cycleDetailId) {
        Feed feed = feedService.search(cycleDetailId);
        List<Comment> comments = commentRepository.findAllByCycleDetailId(feed.getCycleDetailId());
        return comments.stream()
                .map(comment -> new CommentResponse(comment, comment.isCommentByMemberId(tokenPayload.getId())))
                .collect(Collectors.toList());
    }
}
