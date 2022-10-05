package com.woowacourse.smody.push.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.MentionCreateEvent;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.dto.PushNotificationResponse;
import com.woowacourse.smody.push.repository.PushNotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    private final ApplicationEventPublisher publisher;
    private final MemberService memberService;

    @Transactional
    public PushNotification register(PushNotification pushNotification) {
        return pushNotificationRepository.save(pushNotification);
    }

    public List<PushNotificationResponse> searchNotificationsOfMine(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload);
        return pushNotificationRepository.findAllLatest(member, PushStatus.COMPLETE)
                .stream()
                .map(PushNotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        pushNotificationRepository.deleteById(id);
    }

    public Optional<PushNotification> searchSamePathAndStatus(Long pathId, PushStatus status) {
        return pushNotificationRepository.findByPathIdAndPushStatus(pathId, status);
    }

    public void saveNotification(TokenPayload tokenPayload,
                                 MentionNotificationRequest mentionNotificationRequest) {
        List<Long> mentionedIds = mentionNotificationRequest.getMemberIds();
        Long mentioningId = tokenPayload.getId();
        Long cycleDetailId = mentionNotificationRequest.getPathId();
        if (mentionedIds.isEmpty() || mentioningId == null || cycleDetailId == null) {
            return;
        }

        List<Member> mentionedMembers = memberService.searchByIdIn(mentionedIds);
        Member mentioningMember = memberService.searchMember(tokenPayload);
        List<PushNotification> pushNotifications = generatePushNotifications(
                mentionedMembers, mentioningMember, cycleDetailId);
        pushNotifications.forEach(each -> register(each));

        publisher.publishEvent(new MentionCreateEvent(mentionedIds, mentioningId, cycleDetailId));
    }

    private List<PushNotification> generatePushNotifications(List<Member> mentionedMembers, Member mentioningMember, Long pathId) {
        return mentionedMembers.stream()
                .map(each -> buildNotification(mentioningMember, each, pathId))
                .collect(Collectors.toUnmodifiableList());
    }

    public PushNotification buildNotification(Member mentioning, Member mentioned, Long pathId) {
        return PushNotification.builder()
                .message(mentioning.getNickname() + "님께서 회원님을 언급하셨습니다!")
                .pushTime(LocalDateTime.now())
                .pushStatus(PushStatus.IN_COMPLETE)
                .pushCase(PushCase.MENTION)
                .member(mentioned)
                .pathId(pathId)
                .build();
    }
}
