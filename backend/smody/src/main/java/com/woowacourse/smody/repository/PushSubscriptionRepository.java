package com.woowacourse.smody.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

	Optional<PushSubscription> findByEndpoint(String endpoint);

	void deleteByEndpoint(String endpoint);

	List<PushSubscription> findByMemberIn(List<Member> members);
}