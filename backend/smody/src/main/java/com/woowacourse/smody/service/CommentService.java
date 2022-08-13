package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.CommentRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final FeedService feedService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long create(TokenPayload tokenPayload, long cycleDetailId, CommentRequest commentRequest) {
        Member member = memberService.search(tokenPayload);
        String content = commentRequest.getContent();
        CycleDetail cycleDetail = feedService.search(cycleDetailId);
        Comment comment = commentRepository.save(new Comment(cycleDetail, member, content));
        return comment.getId();
    }
}
