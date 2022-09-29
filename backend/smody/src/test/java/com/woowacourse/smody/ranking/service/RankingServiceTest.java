package com.woowacourse.smody.ranking.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
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

    @DisplayName("전체 랭킹 기간을 최신순으로 정렬하여 조회한다.")
    @Test
    void findAll() {
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
}
