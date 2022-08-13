package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.dto.CommentResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.CommentRepository;
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
        CycleDetail cycleDetail = feedService.search(cycleDetailId);
        List<Comment> comments = commentRepository.findAllByCycleDetailId(cycleDetail.getId());
        return comments.stream()
                .map(comment -> new CommentResponse(comment, comment.isCommentByMemberId(tokenPayload.getId())))
                .collect(Collectors.toList());
    }
}
