package com.woowacourse.smody.push.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushNotificationServiceTest extends IntegrationTest {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @DisplayName("발송 가능한 알림을 조회한다.")
    @Test
    void searchPushable() {
        // given
        LocalDateTime now = LocalDateTime.now();

        fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.minusMinutes(2L), PushCase.SUBSCRIPTION);
        fixture.발송_예정_알림_생성(더즈_ID, 2L, now.minusMinutes(3L), PushCase.CHALLENGE);

        fixture.발송_예정_알림_생성(조조그린_ID, 3L, now.plusMinutes(2L), PushCase.CHALLENGE);
        fixture.발송된_알림_생성(토닉_ID, 4L, now.minusHours(1L), PushCase.CHALLENGE);

        // when
        List<PushNotification> actual = pushNotificationService.searchPushable();

        // then
        assertThat(actual)
                .map(PushNotification::getPathId)
                .containsOnly(1L, 2L);
    }

    @DisplayName("알림들을 모두 발송 완료 상태로 변경한다.")
    @Test
    void completeAll() {
        // given
        LocalDateTime now = LocalDateTime.now();

        List<PushNotification> notifications = List.of(
                fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.minusMinutes(2L), PushCase.SUBSCRIPTION),
                fixture.발송_예정_알림_생성(더즈_ID, 2L, now.minusMinutes(3L), PushCase.CHALLENGE),

                fixture.발송_예정_알림_생성(조조그린_ID, 3L, now.minusMinutes(2L), PushCase.CHALLENGE),
                fixture.발송_예정_알림_생성(토닉_ID, 4L, now.minusHours(1L), PushCase.CHALLENGE)
        );

        // when
        pushNotificationService.completeAll(notifications);

        // then
        assertThat(pushNotificationRepository.findAll())
                .map(PushNotification::getPushStatus)
                .containsExactly(PushStatus.COMPLETE, PushStatus.COMPLETE, PushStatus.COMPLETE, PushStatus.COMPLETE);
    }

    @DisplayName("회원으로 보낸 알림을 모두 삭제한다.")
    @Test
    void deleteByMember() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = fixture.회원_조회(조조그린_ID);

        fixture.발송된_알림_생성(조조그린_ID, 1L, now.minusHours(2), PushCase.COMMENT);
        fixture.발송된_알림_생성(조조그린_ID, 1L, now.minusHours(2), PushCase.CHALLENGE);

        fixture.발송_예정_알림_생성(조조그린_ID, 1L, now.plusMinutes(2), PushCase.SUBSCRIPTION);
        fixture.발송된_알림_생성(더즈_ID, 1L, now.plusMinutes(2), PushCase.SUBSCRIPTION);

        // when
        pushNotificationService.deleteCompletedByMember(member);

        // then
        assertThat(pushNotificationRepository.findAll()).hasSize(2);
    }
}
