package com.woowacourse.smody.push.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushNotificationServiceTest extends IntegrationTest {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @DisplayName("푸시 알림을 생성한다.")
    @Test
    void create() {
        // given
        Member member = fixture.회원_조회(조조그린_ID);
        PushNotification pushNotification = new PushNotification(
                "알림", LocalDateTime.now(), PushStatus.IN_COMPLETE, member, PushCase.CHALLENGE, 1L
        );

        // when
        pushNotificationService.create(pushNotification);

        // then
        assertThat(pushNotificationRepository.findById(pushNotification.getId())).isPresent();
    }

    @DisplayName("같은 경로, 상태를 가진 알림을 조회한다.")
    @Test
    void searchSamePathAndStatusAndPushCase() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.발송_예정_알림_생성(조조그린_ID, 1L, now, PushCase.COMMENT);
        PushNotification expected = fixture.발송_예정_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);
        fixture.발송된_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);

        // when
        Optional<PushNotification> pushNotification =
                pushNotificationService.searchByPathAndStatusAndPushCase(
                        1L, PushStatus.IN_COMPLETE, PushCase.CHALLENGE
                );

        // then
        PushNotification actual = pushNotification.get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

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
                .containsExactly(PushStatus.COMPLETE, PushStatus.COMPLETE, PushStatus.COMPLETE,
                        PushStatus.COMPLETE);
    }

    @DisplayName("조회")
    @Test
    void findAllLatestOrderByDesc() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.발송된_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);
        fixture.발송된_알림_생성(조조그린_ID, 2L, now.minusDays(1), PushCase.CHALLENGE);
        fixture.발송된_알림_생성(조조그린_ID, 3L, now.minusDays(2), PushCase.CHALLENGE);
        fixture.발송된_알림_생성(조조그린_ID, 4L, now.minusDays(3), PushCase.CHALLENGE);
        fixture.발송_예정_알림_생성(조조그린_ID, 5L, now.minusDays(4), PushCase.CHALLENGE);
        Member member = fixture.회원_조회(조조그린_ID);

        // when
        List<PushNotification> actual = pushNotificationService.findAllLatestOrderByDesc(member, PushStatus.COMPLETE);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(4),
                () -> assertThat(actual).map(PushNotification::getPathId)
                        .containsExactly(1L, 2L, 3L, 4L)
        );
    }

    @DisplayName("id에 맞는 알림을 삭제할 때")
    @Nested
    class DeleteById {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            LocalDateTime now = LocalDateTime.now();
            PushNotification pushNotification = fixture.발송된_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);

            // when
            pushNotificationService.deleteById(pushNotification.getId());

            // then
            Optional<PushNotification> actual = pushNotificationRepository.findById(pushNotification.getId());
            assertThat(actual).isEmpty();
        }

        @DisplayName("id로 찾지 못했을 때 예외를 발생시킨다.")
        @Test
        void notFound_exception() {
            // then
            assertThatThrownBy(() -> pushNotificationService.deleteById(0L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("ExceptionData")
                    .isEqualTo(ExceptionData.NOT_FOUND_PUSH_NOTIFICATION);
        }
    }

    @DisplayName("보낸 알림을 모두 삭제한다.")
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
