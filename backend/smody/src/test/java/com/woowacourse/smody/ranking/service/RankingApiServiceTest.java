package com.woowacourse.smody.ranking.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RankingApiServiceTest extends IntegrationTest {

    @Autowired
    private RankingApiService rankingApiService;

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
        List<RankingPeriodResponse> actual = rankingApiService.findAllRankingPeriod(
                new PagingParams("startDate:desc", 10)
        );

        // then
        assertThat(actual).map(RankingPeriodResponse::getStartDate)
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
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(토닉_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 300));

        // when
        List<RankingActivityResponse> actual =
                rankingApiService.findAllRankingActivityByPeriodId(rankingPeriod.getId());

        // then
        assertThat(actual).map(RankingActivityResponse::getMemberId)
                .containsExactly(알파_ID, 더즈_ID, 토닉_ID, 조조그린_ID);
    }

    @DisplayName("랭킹 기간의 랭킹 활동을 점수대로 정렬하여 조회한다.")
    @Test
    void findActivityOfMine() {
        // given
        LocalDateTime now = LocalDateTime.now();
        RankingPeriod rankingPeriod = rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));

        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(조조그린_ID), rankingPeriod, 100));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(더즈_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(토닉_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 300));

        // when
        RankingActivityResponse actual = rankingApiService.findActivityOfMine(new TokenPayload(더즈_ID),
                rankingPeriod.getId());

        // then
        assertThat(actual.getMemberId()).isEqualTo(더즈_ID);
    }
}
