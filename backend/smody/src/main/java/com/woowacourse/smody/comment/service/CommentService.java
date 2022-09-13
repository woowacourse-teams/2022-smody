package com.woowacourse.smody.comment.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.feed.repository.FeedRepository;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.event.PushEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long create(TokenPayload tokenPayload, long cycleDetailId, CommentRequest commentRequest) {
        Member member = memberService.search(tokenPayload);
        String content = commentRequest.getContent();
        CycleDetail cycleDetail = feedRepository.findById(cycleDetailId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE_DETAIL));
        Comment comment = commentRepository.save(new Comment(cycleDetail, member, content));

        applicationEventPublisher.publishEvent(new PushEvent(comment, PushCase.COMMENT));
        return comment.getId();
    }

    @Transactional
    public void update(TokenPayload tokenPayload, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = search(commentId);
        validateMember(tokenPayload.getId(), comment);
        comment.updateContent(commentUpdateRequest.getContent());
    }

    @Transactional
    public void delete(TokenPayload tokenPayload, Long commentId) {
        Comment comment = search(commentId);
        validateMember(tokenPayload.getId(), comment);
        commentRepository.delete(comment);
    }

    private Comment search(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_COMMENT));
    }

    private void validateMember(Long memberId, Comment comment) {
        if (!comment.isCommentByMemberId(memberId)) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
    }
}
