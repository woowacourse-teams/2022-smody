package com.woowacourse.smody.member.service;

import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.repository.MemberRepository;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private static final Long NOT_LOGIN_ID = 0L;
    private static final Member NOT_LOGIN_MEMBER = new Member("email", "비회원", "없는 이미지");

    private final MemberRepository memberRepository;
    private final CycleRepository cycleRepository;
    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final PushNotificationRepository pushNotificationRepository;

    public Member search(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }

    public Member searchLoginMember(Long memberId) {
        if (memberId.equals(NOT_LOGIN_ID)) {
            return NOT_LOGIN_MEMBER;
        }
        return search(memberId);
    }

    public List<Member> findAllByFilter(PagingParams pagingParams) {
        return memberRepository.findAllByFilter(pagingParams);
    }

    public List<Member> searchByIdIn(List<Long> ids) {
        return memberRepository.findByIdIn(ids);
    }

    @Transactional
    public void updateByMe(Long memberId, String nickname, String introduction) {
        Member member = search(memberId);
        member.updateNickname(nickname);
        member.updateIntroduction(introduction);
    }

    @Transactional
    public void updateProfileImageByMe(Long memberId, Image image) {
        Member member = search(memberId);
        member.updatePicture(image);
    }

    @Transactional
    public void withdraw(Long memberId) {
        Member member = search(memberId);
        cycleRepository.deleteByMember(member);
        pushNotificationRepository.deleteByMember(member);
        pushSubscriptionRepository.deleteByMember(member);
        memberRepository.delete(member);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member create(Member member) {
        return memberRepository.save(member);
    }
}
