package com.woowacourse.smody.ranking.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.DayOfWeek;
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
        List<RankingPeriodResponse> rankingPeriodResponses = rankingService.findAllPeriod(
                new PagingParams("startDate:desc", 10)
        );

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
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(더즈_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(토닉_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 300));

        // when
        List<RankingActivityResponse> actual = rankingService.findAllActivity(rankingPeriod.getId());

        // then
        assertAll(
                () -> assertThat(actual).map(RankingActivityResponse::getMemberId)
                        .containsExactly(알파_ID, 더즈_ID, 토닉_ID, 조조그린_ID),
                () -> assertThat(actual).map(RankingActivityResponse::getRanking)
                        .containsExactly(1, 2, 2, 4)
        );
    }

    @DisplayName("나의 랭킹 활동을 조회한다.")
    @Test
    void findActivityOfMine() {
        // given
        LocalDateTime now = LocalDateTime.now();
        RankingPeriod rankingPeriod = rankingPeriodRepository.save(new RankingPeriod(now.minusWeeks(1), Duration.WEEK));

        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(조조그린_ID), rankingPeriod, 100));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(더즈_ID), rankingPeriod, 300));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(토닉_ID), rankingPeriod, 200));
        rankingActivityRepository.save(new RankingActivity(fixture.회원_조회(알파_ID), rankingPeriod, 200));

        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);

        // when
        RankingActivityResponse actual = rankingService.findActivityOfMine(tokenPayload, rankingPeriod.getId());

        // then
        assertAll(
                () -> assertThat(actual.getMemberId()).isEqualTo(조조그린_ID),
                () -> assertThat(actual.getRanking()).isEqualTo(4)
        );
    }

    @DisplayName("현재 진행 중인 랭킹 활동이 없으면 생성해서 조회한다.")
    @Test
    void findInProgressActivity() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = fixture.회원_조회(조조그린_ID);

        // when
        List<RankingActivity> activities = rankingService.findInProgressActivity(now, member);

        // then
        assertAll(
                () -> assertThat(activities).isNotEmpty(),
                () -> assertThat(activities)
                        .map(RankingActivity::getRankingPeriod)
                        .map(RankingPeriod::getId)
                        .isNotEmpty()
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

    @DisplayName("현재 진행 중인 랭킹 기간이 없으면 그 주 월요일에 시작하는 주간 랭킹 기간을 생성한다.")
    @Test
    void findInProgressPeriodIsEmpty_createThisWeekPeriod() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        RankingPeriod actual = rankingService.findInProgressPeriod(now).get(0);
        LocalDateTime expected = now.with(DayOfWeek.MONDAY).toLocalDate().atTime(0, 0, 0);
        // then
        assertAll(
                () -> assertThat(actual.getStartDate()).isEqualTo(expected),
                () -> assertThat(actual.getDuration()).isEqualTo(Duration.WEEK)
        );
    }
}
