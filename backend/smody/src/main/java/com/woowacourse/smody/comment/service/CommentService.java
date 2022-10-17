package com.woowacourse.smody.comment.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.feed.repository.FeedRepository;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.util.List;
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
    public Comment create(Long memberId, Long cycleDetailId, String content) {
        Member member = memberService.search(memberId);
        CycleDetail cycleDetail = feedRepository.findById(cycleDetailId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE_DETAIL));
        Comment comment = commentRepository.save(new Comment(cycleDetail, member, content));

        applicationEventPublisher.publishEvent(new CommentCreateEvent(comment));
        return comment;
    }

    @Transactional
    public void update(Long memberId, Long commentId, String content) {
        Comment comment = search(commentId);
        validateMember(memberId, comment);
        comment.updateContent(content);
    }

    @Transactional
    public void delete(Long memberId, Long commentId) {
        Comment comment = search(commentId);
        validateMember(memberId, comment);
        commentRepository.delete(comment);
    }

    public Comment search(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_COMMENT));
    }

    private void validateMember(Long memberId, Comment comment) {
        if (!comment.isCommentByMemberId(memberId)) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
    }

    public List<Comment> findAllByCycleDetailId(Long cycleDetailId) {
        return commentRepository.findAllByCycleDetailId(cycleDetailId);
    }
}
