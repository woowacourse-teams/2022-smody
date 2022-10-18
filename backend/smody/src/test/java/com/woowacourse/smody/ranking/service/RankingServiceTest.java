package com.woowacourse.smody.ranking.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RankingServiceTest extends IntegrationTest {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @DisplayName("전체 랭킹 기간을 최신순으로 정렬하여 조회한다.")
    @Test
    void findAllRankingPeriod() {
        // given
        LocalDateTime now = LocalDate.now().atTime(0, 0, 0);
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(2), Duration.WEEK));
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(3), Duration.WEEK));

        // when
        List<RankingPeriod> rankingPeriods = rankingService.findAllRankingPeriod(
                new PagingParams("startDate:desc", 10)
        );

        // then
        assertThat(rankingPeriods).map(RankingPeriod::getStartDate)
                .containsExactly(now.minusWeeks(1), now.minusWeeks(2), now.minusWeeks(3));
    }

    @DisplayName("랭킹 기간의 랭킹 활동을 점수대로 정렬하여 조회한다.")
    @Test
    void findAllRankingActivityByPeriodId() {
        // given
        LocalDateTime now = LocalDateTime.now();
        RankingPeriod rankingPeriod = rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));

        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(조조그린_ID), rankingPeriod, 100));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(더즈_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 300));

        // when
        List<RankingActivity> actual = rankingService.findAllRankingActivityByPeriodId(
                rankingPeriod.getId());

        // then
        assertAll(
                () -> assertThat(actual).map(rankingActivity -> rankingActivity.getMember().getId())
                        .containsExactly(알파_ID, 더즈_ID, 조조그린_ID)
        );
    }

    @DisplayName("지금 진행 중인 랭킹 기간을 조회한다.")
    @Test
    void findInProgressPeriod() {
        // given
        LocalDateTime now = LocalDateTime.now();
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(2), Duration.WEEK));
        RankingPeriod expected = rankingPeriodRepository.save(new RankingPeriod(now.minusDays(6), Duration.WEEK));
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1).minusSeconds(1), Duration.WEEK));

        // when
        List<RankingPeriod> actual = rankingService.findInProgressPeriod(now);

        // then
        assertThat(actual)
                .map(RankingPeriod::getId)
                .containsExactly(expected.getId());
    }
}
