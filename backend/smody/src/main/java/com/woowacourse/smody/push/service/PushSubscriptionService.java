package com.woowacourse.smody.push.service;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;

    public Optional<PushSubscription> findByEndpoint(String endpoint) {
        return pushSubscriptionRepository.findByEndpoint(endpoint);
    }

    @Transactional
    public PushSubscription create(PushSubscription pushSubscription) {
        return pushSubscriptionRepository.save(pushSubscription);
    }

    @Transactional
    public void delete(PushSubscription pushSubscription) {
        pushSubscriptionRepository.delete(pushSubscription);
    }

    public List<PushSubscription> findByMembers(List<Member> members) {
        return pushSubscriptionRepository.findByMemberIn(members);
    }

    @Transactional
    public void deleteByEndpoint(String endpoint) {
        pushSubscriptionRepository.deleteByEndpoint(endpoint);
    }
}
