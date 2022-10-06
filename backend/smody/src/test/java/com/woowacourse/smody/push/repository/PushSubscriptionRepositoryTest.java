package com.woowacourse.smody.push.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.support.RepositoryTest;
import com.woowacourse.smody.support.ResourceFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PushSubscriptionRepositoryTest extends RepositoryTest {

    @Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;
    @Autowired
    private ResourceFixture fixture;

    @DisplayName("회원 id들로 구독 정보들을 가져 온다.")
    @Test
    void findByMemberIdIn() {
        // given
        Member member1 = fixture.회원_조회(1L);
        PushSubscription pushSubscription1 = new PushSubscription(
                "endPoint1", "p256dh", "auth", member1
        );

        Member member2 = fixture.회원_조회(2L);
        PushSubscription pushSubscription2 = new PushSubscription(
                "endPoint2", "p256dh", "auth", member2
        );

        Member member3 = fixture.회원_조회(3L);
        PushSubscription pushSubscription3 = new PushSubscription(
                "endPoint3", "p256dh", "auth", member3
        );

        PushSubscription pushSubscription4 = new PushSubscription(
                "endPoint4", "p256dh", "auth", member3
        );

        Member member4 = fixture.회원_조회(4L);
        PushSubscription pushSubscription5 = new PushSubscription(
                "endPoint5", "p256dh", "auth", member4
        );

        PushSubscription pushSubscription6 = new PushSubscription(
                "endPoint6", "p256dh", "auth", member4
        );
        pushSubscriptionRepository.saveAll(List.of(
                pushSubscription1, pushSubscription2, pushSubscription3,
                pushSubscription4, pushSubscription5, pushSubscription6
        ));

        // when
        List<PushSubscription> results = pushSubscriptionRepository.findByMemberIn(
                List.of(member1, member2, member3)
        );

        // then
        assertAll(
                () -> assertThat(results).hasSize(4),
                () -> assertThat(results)
                        .containsAll(List.of(
                                pushSubscription1,
                                pushSubscription2,
                                pushSubscription3,
                                pushSubscription4
                        )));
    }
}
