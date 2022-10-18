package com.woowacourse.smody.push.service;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    @Transactional
    public void create(PushNotification pushNotification) {
        pushNotificationRepository.save(pushNotification);
    }

    public Optional<PushNotification> searchByPathAndStatusAndPushCase(Long pathId, PushStatus status, PushCase pushCase) {
        return pushNotificationRepository.findByPathIdAndPushStatusAndPushCase(pathId, status, pushCase);
    }

    public List<PushNotification> searchPushable() {
        LocalDateTime now = LocalDateTime.now();
        return pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE)
            .stream()
            .filter(notification -> notification.isPushable(now))
            .collect(Collectors.toList());
    }

    @Transactional
    public void completeAll(List<PushNotification> notifications) {
        pushNotificationRepository.updatePushStatusIn(notifications, PushStatus.COMPLETE);
    }

    public List<PushNotification> findAllLatestOrderByDesc(Member member, PushStatus pushStatus) {
        return pushNotificationRepository.findAllLatestOrderByDesc(member, pushStatus);
    }

    @Transactional
    public void deleteById(Long pushNotificationId) {
        try {
            pushNotificationRepository.deleteById(pushNotificationId);
        } catch (EmptyResultDataAccessException e) {
            throw new BusinessException(ExceptionData.NOT_FOUND_PUSH_NOTIFICATION);
        }
    }

    @Transactional
    public void deleteCompletedByMember(Member member) {
        pushNotificationRepository.deleteByMemberAndPushStatus(member, PushStatus.COMPLETE);
    }
}
