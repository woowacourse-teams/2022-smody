package com.woowacourse.smody.ranking.event;

import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import com.woowacourse.smody.support.ResourceFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RankingPointEventListenerTest extends IntegrationTest {

    @Autowired
    private RankingPointEventListener eventListener;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @DisplayName("activity가 없을 때 첫 번째로 인증한 멤버는 10점을 얻는다.")
    @Test
    void handle() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        RankingPeriod period = rankingPeriodRepository.save(new RankingPeriod(startTime.minusDays(2), Duration.WEEK));

        Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, ResourceFixture.미라클_모닝_ID, startTime);
        CycleProgressEvent event = new CycleProgressEvent(cycle);

        // when
        eventListener.handle(event);

        // then
        List<RankingActivity> activities = rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(period);

        assertAll(
                () -> assertThat(activities).map(rankingActivity -> rankingActivity.getMember().getId())
                        .containsExactly(조조그린_ID),
                () -> assertThat(activities).map(RankingActivity::getPoint)
                        .containsExactly(10)
        );
    }
}
