package com.woowacourse.smody.comment.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.feed.service.FeedService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
