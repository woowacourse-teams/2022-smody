package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.repository.PushNotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinPushNotificationService implements SmodyVaddinService<PushNotification> {

    private final PushNotificationRepository pushNotificationRepository;

    @Override
    @Transactional
    public void deleteById(Long id) {
        pushNotificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PushNotification save(PushNotification pushNotification) {
        return pushNotificationRepository.save(pushNotification);
    }

    @Override
    public List<PushNotification> findAll() {
        return pushNotificationRepository.findAll();
    }
}
