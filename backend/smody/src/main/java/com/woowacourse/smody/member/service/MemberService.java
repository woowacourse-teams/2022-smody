package com.woowacourse.smody.member.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.member.repository.MemberRepository;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private static final TokenPayload NOT_LOGIN = new TokenPayload(0L);
    private static final Member NOT_LOGIN_MEMBER = new Member("email", "비회원", "없는 이미지");

    private final MemberRepository memberRepository;
    private final CycleRepository cycleRepository;
    private final ImageStrategy imageStrategy;

    private final PushSubscriptionRepository pushSubscriptionRepository;

    private final PushNotificationRepository pushNotificationRepository;

    public MemberResponse searchMyInfo(TokenPayload tokenPayload) {
        Member member = search(tokenPayload);
        return new MemberResponse(member);
    }

    @Transactional
    public void updateMyInfo(TokenPayload tokenPayload, MemberUpdateRequest updateRequest) {
        Member member = search(tokenPayload);
        member.updateNickname(updateRequest.getNickname());
        member.updateIntroduction(updateRequest.getIntroduction());
    }

    @Transactional
    public void withdraw(TokenPayload tokenPayload) {
        Member member = search(tokenPayload);
        cycleRepository.deleteByMember(member);
        pushNotificationRepository.deleteByMember(member);
        pushSubscriptionRepository.deleteByMember(member);
        memberRepository.delete(member);
    }

    public Member search(TokenPayload tokenPayload) {
        return memberRepository.findById(tokenPayload.getId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }

    @Transactional
    public void updateProfileImage(TokenPayload tokenPayload, MultipartFile profileImage) {
        Member member = search(tokenPayload);
        member.updatePicture(new Image(profileImage, imageStrategy));
    }

    public Member findMember(TokenPayload tokenPayload) {
        if (tokenPayload.equals(NOT_LOGIN)) {
            return NOT_LOGIN_MEMBER;
        }
        return search(tokenPayload);
    }
}
