package com.woowacourse.smody.push.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.push.dto.PushNotificationResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationApiService {

    private final PushNotificationService pushNotificationService;
    private final MemberService memberService;

    public List<PushNotificationResponse> findCompleteNotificationsByMe(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload.getId());
        List<PushNotification> pushNotifications =
                pushNotificationService.findAllLatestOrderByDesc(member, PushStatus.COMPLETE);
        return pushNotifications.stream()
                .map(PushNotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        pushNotificationService.deleteById(id);
    }

    @Transactional
    public void createMentionNotification(TokenPayload tokenPayload,
                                          MentionNotificationRequest mentionNotificationRequest) {
        List<Member> mentionedMembers = memberService.searchByIdIn(mentionNotificationRequest.getMemberIds());
        Member mentioningMember = memberService.searchLoginMember(tokenPayload.getId());
        List<PushNotification> pushNotifications = generatePushNotifications(
                mentionedMembers, mentioningMember, mentionNotificationRequest.getPathId()
        );
        pushNotifications.forEach(pushNotificationService::create);
    }

    private List<PushNotification> generatePushNotifications(List<Member> mentionedMembers,
                                                             Member mentioningMember,
                                                             Long pathId) {
        return mentionedMembers.stream()
                .map(mentionedMember -> buildNotification(mentioningMember, mentionedMember, pathId))
                .collect(Collectors.toUnmodifiableList());
    }

    private PushNotification buildNotification(Member mentioning, Member mentionedMember, Long pathId) {
        return PushNotification.builder()
                .message(mentioning.getNickname() + "님께서 회원님을 언급하셨습니다!")
                .pushTime(LocalDateTime.now())
                .pushStatus(PushStatus.IN_COMPLETE)
                .pushCase(PushCase.MENTION)
                .member(mentionedMember)
                .pathId(pathId)
                .build();
    }

    @Transactional
    public void deleteCompleteNotificationsByMe(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload.getId());
        pushNotificationService.deleteCompletedByMember(member);
    }
}
