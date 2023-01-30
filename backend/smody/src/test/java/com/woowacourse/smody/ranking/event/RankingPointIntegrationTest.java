package com.woowacourse.smody.ranking.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.service.CycleApiService;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.EventListenerMockTest;

class RankingPointIntegrationTest extends EventListenerMockTest {

    @Autowired
    private CycleApiService cycleApiService;

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @DisplayName("주간 랭킹에 첫 사이클 인증을 하면 포인트가 늘어난다.")
    @ParameterizedTest
    @CsvSource({"NOTHING,0,10", "FIRST,1,30", "SECOND,2,60"})
    void progress_first(Progress progress, Long day, Integer expected) {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, progress, now);
        RankingPeriod period = rankingPeriodRepository.save(new RankingPeriod(now.minusDays(2), Duration.WEEK));

        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        // when
        cycleApiService.increaseProgress(
            new TokenPayload(조조그린_ID),
            new ProgressRequest(cycle.getId(), now.plusDays(day).plusMinutes(1L), MULTIPART_FILE, "인증")
        );

        // then
        verify(rankingPointEventListener)
            .handle(any(CycleProgressEvent.class));
    }
}
