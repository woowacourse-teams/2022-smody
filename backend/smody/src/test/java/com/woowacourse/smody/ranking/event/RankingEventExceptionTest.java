package com.woowacourse.smody.ranking.event;

import static com.woowacourse.smody.support.ResourceFixture.MULTIPART_FILE;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class RankingEventExceptionTest extends IntegrationTest {

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @Autowired
    private CycleService cycleService;

    private EntityManager em;

    @MockBean
    private RankingPointEventListener rankingPointEventListener;

    @DisplayName("랭킹에 예외가 발생해도 인증은 되어야 한다.")
    @Test
    void progress_rankException() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);
        RankingPeriod period = rankingPeriodRepository.save(new RankingPeriod(now.minusDays(2), Duration.WEEK));


        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");

        doThrow(new RuntimeException("랭킹 로직에 예상치 못한 예외 발생!"))
                .when(rankingPointEventListener).handle(any(CycleProgressEvent.class));

        // when
        synchronize(() -> cycleService.increaseProgress(
                new TokenPayload(조조그린_ID),
                new ProgressRequest(cycle.getId(), now.plusMinutes(1L), MULTIPART_FILE, "인증")
        ));

        // then
        List<RankingActivity> activities = rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(period);
        Cycle actual = cycleService.findAllByMemberAndFilter(fixture.회원_조회(조조그린_ID), new PagingParams()).get(0);

        assertAll(
                () -> assertThat(activities).isEmpty(),
                () -> assertThat(actual.getProgress()).isEqualTo(Progress.FIRST),
                () -> assertThat(actual.getCycleDetailsOrderByProgress()).hasSize(1)
        );
    }
}
