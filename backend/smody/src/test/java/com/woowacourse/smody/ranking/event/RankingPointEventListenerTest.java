package com.woowacourse.smody.ranking.event;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class RankingPointEventListenerTest extends IntegrationTest {

    @Autowired
    private RankingPointEventListener eventListener;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @DisplayName("랭킹 기간이 없고 처음 인증했을 때 progress에 따라 점수를 얻는다.")
    @ParameterizedTest
    @CsvSource({"FIRST,10", "SECOND,30", "SUCCESS,60"})
    void handle_firstRank_notPeriod(Progress progress, Integer expected) throws InterruptedException {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, progress, startTime);
        CycleProgressEvent event = new CycleProgressEvent(cycle);

        // when
        synchronize(() -> eventListener.handle(event));

        // then
        List<RankingActivity> activities = rankingActivityRepository.findAll();

        assertAll(
                () -> assertThat(activities).map(rankingActivity -> rankingActivity.getMember().getId())
                        .containsExactly(조조그린_ID),
                () -> assertThat(activities).map(RankingActivity::getPoint)
                        .containsExactly(expected)
        );
    }

    @DisplayName("랭킹 기간이 존재하고 처음 인증했을 때 progress에 따라 점수를 얻는다.")
    @ParameterizedTest
    @CsvSource({"FIRST,10", "SECOND,30", "SUCCESS,60"})
    void handle_firstRank(Progress progress, Integer expected) throws InterruptedException {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        RankingPeriod period = rankingPeriodRepository.save(new RankingPeriod(startTime.minusDays(2), Duration.WEEK));
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, progress, startTime);
        CycleProgressEvent event = new CycleProgressEvent(cycle);

        // when
        synchronize(() -> eventListener.handle(event));

        // then
        List<RankingActivity> activities = rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(period);

        assertAll(
                () -> assertThat(activities).map(rankingActivity -> rankingActivity.getMember().getId())
                        .containsExactly(조조그린_ID),
                () -> assertThat(activities).map(RankingActivity::getPoint)
                        .containsExactly(expected)
        );
    }

    @DisplayName("랭킹 기간이 있고 두번째 인증했을 때 progress에 따라 점수를 얻는다.")
    @ParameterizedTest
    @CsvSource({"FIRST,110", "SECOND,130", "SUCCESS,160"})
    void handle_secondRank(Progress progress, Integer expected) throws InterruptedException {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        RankingPeriod period = rankingPeriodRepository.save(new RankingPeriod(startTime.minusDays(2), Duration.WEEK));

        Member member = fixture.회원_조회(조조그린_ID);

        RankingActivity activity = rankingActivityRepository.save(new RankingActivity(member, period, 100));

        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, progress, startTime);
        CycleProgressEvent event = new CycleProgressEvent(cycle);

        // when
        synchronize(() -> eventListener.handle(event));

        // then
        List<RankingActivity> activities = rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(period);

        assertAll(
                () -> assertThat(activities).map(rankingActivity -> rankingActivity.getMember().getId())
                        .containsExactly(조조그린_ID),
                () -> assertThat(activities).map(RankingActivity::getId)
                        .containsExactly(activity.getId()),
                () -> assertThat(activities).map(RankingActivity::getPoint)
                        .containsExactly(expected)
        );
    }

    @DisplayName("똑같은 날에 똑같은 기간으로 기간을 생성하지 못한다.")
    @Test
    void unique() throws InterruptedException {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        Cycle cycle1 = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.FIRST, startTime);
        Cycle cycle2 = fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, startTime);

        CycleProgressEvent event1 = new CycleProgressEvent(cycle1);
        CycleProgressEvent event2 = new CycleProgressEvent(cycle2);

        // when
        synchronize(() -> {
            eventListener.handle(event1);
            eventListener.handle(event2);
        });

        assertAll(
                () -> assertThat(rankingPeriodRepository.findAll()).hasSize(1),
                () -> assertThat(rankingActivityRepository.findAll()).hasSize(2)
        );
    }
}
