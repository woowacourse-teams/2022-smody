package com.woowacourse.smody.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowacourse.smody.domain.PushNotification;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
}
