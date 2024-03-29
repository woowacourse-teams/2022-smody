package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.FORMATTER;
import static com.woowacourse.smody.support.ResourceFixture.MULTIPART_FILE;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.service.CycleApiService;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChallengePushEventIntegrationTest extends IntegrationTest {

    @Autowired
    private CycleApiService cycleApiService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @DisplayName("새로운 사이클을 생성하면 발송 예정인 알림이 저장된다.")
    @Test
    void cycleCreate_pushNotification() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        AtomicReference<Long> pathId = new AtomicReference<>();

        synchronize(() -> {
            pathId.set(cycleApiService.create(
                    new TokenPayload(조조그린_ID),
                    new CycleRequest(now, 스모디_방문하기_ID)
            ));
        });

        // then
        LocalDateTime pushTime = now
                .plusDays(1L)
                .minusHours(3L);
        Optional<Cycle> cycle = cycleService.findById(pathId.get());
        PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
        assertAll(
                () -> assertThat(cycle).isPresent(),
                () -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
                () -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
                () -> assertThat(pushNotification.getPushTime().format(FORMATTER))
                        .isEqualTo(pushTime.format(FORMATTER)),
                () -> assertThat(pushNotification.getMessage()).isEqualTo("스모디 방문하기 인증까지 얼마 안남았어요~"),
                () -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
                () -> assertThat(pushNotification.getPathId()).isEqualTo(pathId.get())
        );
    }

    @DisplayName("사이클을 진행하면 발송 예정인 알림이 저장된다.")
    @Test
    void increaseProgress_sendNotification() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now.minusDays(1L));

        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        // when

        synchronize(() -> cycleApiService.increaseProgress(
                new TokenPayload(조조그린_ID),
                new ProgressRequest(cycle.getId(), now.plusMinutes(1L), MULTIPART_FILE, "인증")
        ));

        // then
        LocalDateTime pushTime = now
                .plusDays(2L)
                .minusHours(3L);
        PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
        assertAll(
                () -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
                () -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
                () -> assertThat(pushNotification.getPushTime().format(FORMATTER))
                        .isEqualTo(pushTime.format(FORMATTER)),
                () -> assertThat(pushNotification.getMessage()).isEqualTo("미라클 모닝 인증까지 얼마 안남았어요~"),
                () -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
                () -> assertThat(pushNotification.getPathId()).isEqualTo(cycle.getId())
        );
    }

    @DisplayName("사이클을 성공하면 알림이 저장되지 않는다.")
    @Test
    void increaseProgressSuccess_sendNotification_no() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));

        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        // when
        synchronize(() -> cycleApiService.increaseProgress(
                new TokenPayload(조조그린_ID),
                new ProgressRequest(cycle.getId(), now.plusMinutes(1L), MULTIPART_FILE, "인증")
        ));

        // then
        assertThat(pushNotificationRepository.findAll()).isEmpty();
    }
}
