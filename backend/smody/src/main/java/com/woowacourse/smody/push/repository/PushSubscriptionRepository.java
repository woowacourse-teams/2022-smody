package com.woowacourse.smody.push.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    Optional<PushSubscription> findByEndpoint(String endpoint);

    void deleteByEndpoint(String endpoint);

    List<PushSubscription> findByMemberIn(List<Member> members);

    @Modifying(clearAutomatically = true)
    @Query("delete from PushSubscription ps where ps.member = :member")
    void deleteByMember(@Param("member") Member member);
}
