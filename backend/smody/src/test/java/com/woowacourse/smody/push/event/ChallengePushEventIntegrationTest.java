package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.service.CycleApiService;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.support.EventListenerMockTest;

class ChallengePushEventIntegrationTest extends EventListenerMockTest {

    @Autowired
    private CycleApiService cycleApiService;

    @Autowired
    private CycleService cycleService;

    @DisplayName("새로운 사이클을 생성하면 발송 예정인 알림이 저장된다.")
    @Test
    void cycleCreate_pushNotification() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when

        Long pathId = cycleApiService.create(
            new TokenPayload(조조그린_ID),
            new CycleRequest(now, 스모디_방문하기_ID)
        );

        // then
        Optional<Cycle> cycle = cycleService.findById(pathId);
        assertAll(
                () -> assertThat(cycle).isPresent(),
                () -> verify(challengePushEventListener)
                    .handle(any(CycleCreateEvent.class))
        );
    }

    @DisplayName("사이클을 진행하면 발송 예정인 알림이 저장된다.")
    @Test
    void increaseProgress_sendNotification() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now.minusDays(1L));

        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        // when
        cycleApiService.increaseProgress(
            new TokenPayload(조조그린_ID),
            new ProgressRequest(cycle.getId(), now.plusMinutes(1L), MULTIPART_FILE, "인증")
        );

        // then
        Optional<Cycle> progressed = cycleService.findById(cycle.getId());
        assertAll(
                () -> assertThat(progressed)
                    .map(Cycle::getProgress)
                    .get()
                    .isEqualTo(Progress.SECOND),
                () -> verify(challengePushEventListener)
                    .handle(any(CycleProgressEvent.class))
        );
    }

    @DisplayName("사이클을 성공하면 알림이 저장되지 않는다.")
    @Test
    void increaseProgressSuccess_sendNotification_no() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));

        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        // when
        cycleApiService.increaseProgress(
            new TokenPayload(조조그린_ID),
            new ProgressRequest(cycle.getId(), now.plusMinutes(1L), MULTIPART_FILE, "인증")
        );

        // then
        verify(challengePushEventListener)
            .handle(any(CycleProgressEvent.class));
    }
}
