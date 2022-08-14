package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    List<PushNotification> findByPushStatus(PushStatus pushStatus);

	Optional<PushNotification> findByPathIdAndPushStatus(Long pathId, PushStatus pushStatus);

	void deleteByMember(Member member);
}
