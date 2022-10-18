package com.woowacourse.smody.push.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    List<PushNotification> findByPushStatus(PushStatus pushStatus);

    Optional<PushNotification> findByPathIdAndPushStatusAndPushCase(Long pathId, PushStatus pushStatus, PushCase pushCase);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from PushNotification pn where pn.member = :member")
    void deleteByMember(@Param("member") Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from PushNotification pn "
            + "where pn.member = :member and pn.pushStatus = :pushStatus")
    void deleteByMemberAndPushStatus(@Param("member") Member member, @Param("pushStatus") PushStatus pushStatus);

    @Query("select pn from PushNotification pn "
            + "where pn.member = :member and pn.pushStatus = :pushStatus "
            + "order by pn.pushTime desc")
    List<PushNotification> findAllLatestOrderByDesc(@Param("member") Member member,
                                                    @Param("pushStatus") PushStatus pushStatus);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update PushNotification pn set pn.pushStatus = :pushStatus where pn in :notifications")
    void updatePushStatusIn(@Param("notifications") List<PushNotification> notifications,
                            @Param("pushStatus") PushStatus pushStatus);
}
