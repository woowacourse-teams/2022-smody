package com.woowacourse.smody.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

	List<PushNotification> findByPushStatus(PushStatus pushStatus);
}
