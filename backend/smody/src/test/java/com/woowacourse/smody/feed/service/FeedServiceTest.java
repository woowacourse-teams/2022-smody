package com.woowacourse.smody.feed.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FeedServiceTest extends IntegrationTest {

    @Autowired
    private FeedService feedService;

    @DisplayName("모든 피드 조회 시 지정된 커서 Id 를 기준으로 최근에 생성된 데이터를 조회한다.")
    @Test
    void findAll() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle1 = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today);
        Cycle cycle2 = fixture.사이클_생성_SUCCESS(조조그린_ID, 오늘의_운동_ID, today);
        Cycle cycle3 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, today);
        Cycle cycle4 = fixture.사이클_생성_SUCCESS(조조그린_ID, 알고리즘_풀기_ID, today);

        // when
        List<Feed> actual = feedService.findAll(
                new PagingParams("latest", 10, cycle1.getCycleDetailsOrderByProgress().get(2).getId())
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(actual.stream()
                        .map(Feed::getCycleDetailId)
                        .collect(Collectors.toList())).containsExactly(
                        cycle4.getCycleDetailsOrderByProgress().get(2).getId(),
                        cycle3.getCycleDetailsOrderByProgress().get(2).getId(),
                        cycle2.getCycleDetailsOrderByProgress().get(2).getId(),
                        cycle4.getCycleDetailsOrderByProgress().get(1).getId(),
                        cycle3.getCycleDetailsOrderByProgress().get(1).getId(),
                        cycle2.getCycleDetailsOrderByProgress().get(1).getId(),
                        cycle1.getCycleDetailsOrderByProgress().get(1).getId(),
                        cycle4.getCycleDetailsOrderByProgress().get(0).getId(),
                        cycle3.getCycleDetailsOrderByProgress().get(0).getId(),
                        cycle2.getCycleDetailsOrderByProgress().get(0).getId()
                )
        );
    }

    @DisplayName("cycleDetailId 로 feed 를 조회한다.")
    @Test
    void findById() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today);

        // when
        List<CycleDetail> cycleDetails = cycle.getCycleDetailsOrderByProgress();
        Long cycleDetailId = cycleDetails.get(0).getId();
        Feed actual = feedService.search(cycleDetailId);

        // then
        assertAll(
                () -> assertThat(actual.getCycleDetailId()).isEqualTo(cycleDetailId),
                () -> assertThat(actual.getProgress().getCount()).isEqualTo(1)
        );
    }
}
