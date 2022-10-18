package com.woowacourse.smody.push.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.support.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushSubscriptionServiceTest extends IntegrationTest {

    @Autowired
    private PushSubscriptionService pushSubscriptionService;

    @DisplayName("endpoint로 알림 구독을 찾는다.")
    @Test
    void findByEndpoint() {
        // given
        fixture.알림_구독(조조그린_ID, "endpoint1");

        // when
        Optional<PushSubscription> actual = pushSubscriptionService.findByEndpoint("endpoint1");

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getEndpoint()).isEqualTo("endpoint1")
        );
    }

    @DisplayName("알림 구독을 생성한다.")
    @Test
    void create() {
        // when
        Member member = fixture.회원_조회(조조그린_ID);
        pushSubscriptionService.create(
                new PushSubscription("endpoint", "p256dh", "auth", member));

        // then
        assertThat(pushSubscriptionService.findByEndpoint("endpoint")).isPresent();
    }

    @DisplayName("알림을 삭제한다.")
    @Test
    void delete() {
        // given
        PushSubscription pushSubscription = fixture.알림_구독(조조그린_ID, "endpoint");

        // when
        pushSubscriptionService.delete(pushSubscription);

        // then
        assertThat(pushSubscriptionService.findByEndpoint("endpoint")).isEmpty();
    }

    @DisplayName("회원들의 알림 구독을 조회한다.")
    @Test
    void searchByMembers() {
        // given
        Member member1 = fixture.회원_조회(조조그린_ID);
        Member member2 = fixture.회원_조회(토닉_ID);
        Member member3 = fixture.회원_조회(더즈_ID);
        fixture.알림_구독(조조그린_ID, "endpoint1");
        fixture.알림_구독(토닉_ID, "endpoint2");
        fixture.알림_구독(더즈_ID, "endpoint3");

        // when
        List<PushSubscription> pushSubscriptions = pushSubscriptionService.searchByMembers(
                List.of(member1, member2, member3));

        // then
        assertAll(
                () -> assertThat(pushSubscriptions).hasSize(3),
                () -> assertThat(pushSubscriptions).map(pushSubscription -> pushSubscription.getMember().getId())
                        .contains(조조그린_ID, 토닉_ID, 더즈_ID)
        );
    }

    @DisplayName("endpoint에 해당하는 알림 구독을 삭제한다.")
    @Test
    void deleteByEndpoint() {
        // given
        fixture.알림_구독(조조그린_ID, "endpoint");

        // when
        pushSubscriptionService.deleteByEndpoint("endpoint");

        // then
        assertThat(pushSubscriptionService.findByEndpoint("endpoint")).isEmpty();
    }
}
