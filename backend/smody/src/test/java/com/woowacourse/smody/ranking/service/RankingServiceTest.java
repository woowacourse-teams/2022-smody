package com.woowacourse.smody.ranking.service;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import com.woowacourse.smody.support.ResourceFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RankingServiceTest extends IntegrationTest {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @DisplayName("전체 랭킹 기간을 최신순으로 정렬하여 조회한다.")
    @Test
    void findAllPeriodLatest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(2), Duration.WEEK));
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));
        rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(3), Duration.WEEK));

        // when
        List<RankingPeriodResponse> rankingPeriodResponses = rankingService.findAllPeriodLatest();

        // then
        assertThat(rankingPeriodResponses).map(RankingPeriodResponse::getStartDate)
                .containsExactly(now.minusWeeks(1), now.minusWeeks(2), now.minusWeeks(3));
    }

    @DisplayName("랭킹 기간의 랭킹 활동을 순위대로 정렬하여 조회한다.")
    @Test
    void findAllActivity() {
        // given
        LocalDateTime now = LocalDateTime.now();
        RankingPeriod rankingPeriod = rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));

        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(조조그린_ID), rankingPeriod, 100));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(더즈_ID), rankingPeriod, 300));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(토닉_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 200));

        // when
        List<RankingActivityResponse> actual = rankingService.findAllActivity(rankingPeriod.getId());

        // then
        assertAll(
                () -> assertThat(actual).map(RankingActivityResponse::getMemberId)
                        .containsExactly(더즈_ID, 토닉_ID, 알파_ID, 조조그린_ID),
                () -> assertThat(actual).map(RankingActivityResponse::getRanking)
                        .containsExactly(1, 2, 2, 3)
        );
    }
}
