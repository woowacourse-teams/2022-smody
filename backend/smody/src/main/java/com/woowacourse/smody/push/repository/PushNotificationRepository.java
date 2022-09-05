package com.woowacourse.smody.push.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    List<PushNotification> findByPushStatus(PushStatus pushStatus);

    Optional<PushNotification> findByPathIdAndPushStatus(Long pathId, PushStatus pushStatus);

    void deleteByMember(Member member);

    @Query("select pn from PushNotification pn where pn.member = :member and pn.pushStatus = :pushStatus "
            + "order by pn.pushTime desc")
    List<PushNotification> findAllLatest(@Param("member") Member member, @Param("pushStatus") PushStatus pushStatus);
}
