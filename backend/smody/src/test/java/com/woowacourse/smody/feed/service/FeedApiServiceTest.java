package com.woowacourse.smody.feed.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.feed.dto.FeedResponse;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FeedApiServiceTest extends IntegrationTest {

    @Autowired
    private FeedApiService feedApiService;

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
        List<FeedResponse> feedResponses = feedApiService.findAll(
                new PagingParams("latest", 10, cycle1.getCycleDetailsOrderByProgress().get(2).getId())
        );

        // then
        assertAll(
                () -> assertThat(feedResponses).hasSize(10),
                () -> assertThat(feedResponses.stream()
                        .map(FeedResponse::getCycleDetailId)
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

    @DisplayName("cursorId 값이 null 로 들어오면 가장 최신순으로 조회한다")
    @Test
    void findAll_cursorIdNull() {
        // given
        LocalDateTime today = LocalDateTime.now().minusDays(3);
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today);

        // when
        List<FeedResponse> feedResponses = feedApiService.findAll(new PagingParams("latest", 10));

        //then
        assertAll(
                () -> assertThat(feedResponses).hasSize(3),
                () -> assertThat(feedResponses.stream()
                        .map(FeedResponse::getDescription)
                        .collect(Collectors.toList())).containsExactly("third", "second", "first")
        );
    }

    @DisplayName("모든 피드 조회 시 사이클의 몇 번째 인증인지 알려준다.")
    @Test
    void findAll_WithProgress() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle1 = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today);
        Cycle cycle2 = fixture.사이클_생성_SUCCESS(조조그린_ID, 오늘의_운동_ID, today);
        Cycle cycle3 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, today);
        Cycle cycle4 = fixture.사이클_생성_SUCCESS(조조그린_ID, 알고리즘_풀기_ID, today);

        // when
        List<FeedResponse> feedResponses = feedApiService.findAll(
                new PagingParams("latest", 10, cycle1.getCycleDetailsOrderByProgress().get(2).getId())
        );

        // then
        assertAll(
                () -> assertThat(feedResponses).hasSize(10),
                () -> assertThat(feedResponses)
                        .map(FeedResponse::getProgressCount)
                        .containsExactly(3, 3, 3, 2, 2, 2, 2, 1, 1, 1)
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
        FeedResponse feedResponse = feedApiService.findById(cycleDetailId);

        // then
        assertAll(
                () -> assertThat(feedResponse.getCycleDetailId()).isEqualTo(cycleDetailId),
                () -> assertThat(feedResponse.getProgressCount()).isEqualTo(1)
        );
    }

    @DisplayName("단건 조회 시 CycleDetail 을 찾지 못했을 경우 예외 발생")
    @Test
    void findById_notExistCycleDetail() {
        assertThatThrownBy(() -> feedApiService.findById(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CYCLE_DETAIL);
    }
}
