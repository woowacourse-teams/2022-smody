package com.woowacourse.smody.cycle.service;

import static com.woowacourse.smody.support.ResourceFixture.FORMATTER;
import static com.woowacourse.smody.support.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.support.ResourceFixture.MULTIPART_FILE;
import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.dto.ProgressResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class CycleApiServiceTest extends IntegrationTest {

    @Autowired
    private CycleApiService cycleApiService;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void init() {
        given(imageStrategy.extractUrl(any()))
                .willReturn("fakeUrl");
    }

    @DisplayName("사이클을 생성한다.")
    @Test
    void create() {
        // when
        LocalDateTime time = LocalDateTime.of(1996, 8, 30, 8, 0);
        CycleRequest cycleRequest = new CycleRequest(time, 스모디_방문하기_ID);
        Long id = cycleApiService.create(new TokenPayload(조조그린_ID), cycleRequest);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("유효한 시간일때 사이클의 진행도를 증가시킨다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:00,1",
            "NOTHING,2022-01-01T23:59:59,1",
            "FIRST,2022-01-02T00:00:00,2",
            "FIRST,2022-01-02T23:59:59,2",
            "SECOND,2022-01-03T00:00:00,3",
            "SECOND,2022-01-03T23:59:59,3"
    })
    void increaseProgress(Progress progress, LocalDateTime progressTime, int expected) {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        Cycle cycle = fixture.사이클_생성(
                조조그린_ID,
                스모디_방문하기_ID,
                progress,
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        ProgressRequest request = new ProgressRequest(cycle.getId(), progressTime, imageFile, "인증 완료");

        // when
        ProgressResponse progressResponse = cycleApiService.increaseProgress(tokenPayload, request);

        // then
        assertThat(progressResponse.getProgressCount()).isEqualTo(expected);
    }

    @DisplayName("진행도를 증가 시킬 때 유효하지 않은 시간인 경우 예외 발생")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2021-12-31T23:59:59",
            "NOTHING,2022-01-02T00:00:00",
            "FIRST,2022-01-01T23:59:59",
            "FIRST,2022-01-03T00:00:00",
            "SECOND,2022-01-02T23:59:59",
            "SECOND,2022-01-04T00:00:00"
    })
    void increaseProgress_failWithTime(Progress progress, LocalDateTime invalidTime) {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        ProgressRequest request = new ProgressRequest(cycle.getId(), invalidTime, MULTIPART_FILE, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleApiService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("하루에 사이클의 진행을 두번 할 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:00,2022-01-01T23:59:59",
            "FIRST,2022-01-02T00:00:00,2022-01-02T23:59:59"
    })
    void increaseProgress_twoTimeInOneDay(Progress progress, LocalDateTime progressTime) {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        ProgressRequest request = new ProgressRequest(cycle.getId(), progressTime, MULTIPART_FILE, "인증 완료");
        cycleApiService.increaseProgress(tokenPayload, request);

        // when then
        assertThatThrownBy(() ->
                cycleApiService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("진행도를 증가 시킬 때 사이클을 찾지 못했을 경우 예외 발생")
    @Test
    void increaseProgress_notExistCycle() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        ProgressRequest request = new ProgressRequest(1000L, LocalDateTime.now(), MULTIPART_FILE, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleApiService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CYCLE);
    }

    @DisplayName("진행도를 증가 시킬 때 권한에 맞지 않은 cycle일 경우 예외 발생")
    @Test
    void increaseProgress_unauthorized() {
        // given
        TokenPayload tokenPayload = new TokenPayload(알파_ID);
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.NOTHING, now);
        ProgressRequest request = new ProgressRequest(cycle.getId(), now.plusSeconds(1), MULTIPART_FILE, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleApiService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.UNAUTHORIZED_MEMBER);
    }

    @DisplayName("진행 중인 자신의 모든 사이클을 조회")
    @Test
    void findAllInProgressOfMine() {
        // given
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.minusHours(1)); // 2, 1
        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));

        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now.minusHours(2)); // 1, 2
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(4L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));

        fixture.사이클_생성_SUCCESS(더즈_ID, 미라클_모닝_ID, now.minusDays(6L));

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now.plusSeconds(1L)); // 3, 0

        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);

        // when
        List<InProgressCycleResponse> actual = cycleApiService.findInProgressByMe(
                tokenPayload, now, new PagingParams(null, null, 0L, null)
        );

        // then
        assertThat(actual).map(InProgressCycleResponse::getSuccessCount)
                .containsExactly(2, 1, 0);
    }

    @DisplayName("id로 사이클 조회 시 성공 회수와 함께 조회")
    @Test
    void findWithSuccessCountById() {
        // given
        Cycle inProgress = fixture.사이클_생성_SECOND(조조그린_ID, 스모디_방문하기_ID, now);

        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_SECOND(조조그린_ID, 스모디_방문하기_ID, now.minusDays(6L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(9L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(12L));

        // when
        CycleResponse cycleResponse = cycleApiService.findWithSuccessCountById(inProgress.getId());

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(inProgress.getId()),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(inProgress.getChallenge().getId()),
                () -> assertThat(cycleResponse.getChallengeName()).isEqualTo(inProgress.getChallenge().getName()),
                () -> assertThat(cycleResponse.getProgressCount()).isEqualTo(inProgress.getProgress().getCount()),
                () -> assertThat(cycleResponse.getStartTime().format(FORMATTER))
                        .isEqualTo(inProgress.getStartTime().format(FORMATTER)),
                () -> assertThat(cycleResponse.getSuccessCount()).isEqualTo(2),
                () -> assertThat(cycleResponse.getCycleDetails().get(0).getProgressTime().format(FORMATTER)).isEqualTo(
                        now.plusSeconds(1L).format(FORMATTER)),
                () -> assertThat(cycleResponse.getCycleDetails().get(1).getProgressTime().format(FORMATTER))
                        .isEqualTo(now.plusDays(1L).format(FORMATTER))

        );
    }

    @DisplayName("나의 사이클 중")
    @Nested
    class NestedTest {

        Cycle inProgress1;
        Cycle inProgress2;
        Cycle inProgress3;
        Cycle proceed1;
        Cycle proceed2;
        Cycle failed;
        Cycle success;
        TokenPayload tokenPayload;

        @BeforeEach
        void setUp() {
            inProgress1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusHours(43L));
            inProgress2 = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now.minusHours(5L));
            inProgress3 = fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now);
            proceed1 = fixture.사이클_생성_FIRST(조조그린_ID, 알고리즘_풀기_ID, now);
            proceed2 = fixture.사이클_생성_SECOND(조조그린_ID, JPA_공부_ID, now.minusHours(36L));
            failed = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.minusHours(120L));
            success = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusHours(1000L));
            tokenPayload = new TokenPayload(조조그린_ID);
        }

        @DisplayName("진행 중인 모든 사이클을 남은 인증 시간 기준으로 오름차순 정렬")
        @Test
        void findAllInProgress_sort() {
            // when
            List<InProgressCycleResponse> actual = cycleApiService.findInProgressByMe(
                    tokenPayload, now, new PagingParams(null, null, 0L, null));

            // then
            assertThat(actual)
                    .map(InProgressCycleResponse::getCycleId)
                    .containsExactly(inProgress1.getId(), inProgress2.getId(), inProgress3.getId(),
                            proceed2.getId(), proceed1.getId());
        }

        @DisplayName("진행 중인 모든 사이클을 0페이지부터 3개만 조회")
        @Test
        void findAllInProgress_pagingFullSize() {
            // when
            List<InProgressCycleResponse> actual = cycleApiService.findInProgressByMe(
                    tokenPayload, now, new PagingParams(null, 3, 0L, null));

            // then
            assertThat(actual)
                    .map(InProgressCycleResponse::getCycleId)
                    .containsExactly(inProgress1.getId(), inProgress2.getId(), inProgress3.getId());
        }

        @DisplayName("진행 중인 모든 사이클을 1페이지부터 2개만 조회")
        @Test
        void findAllInProgress_pagingPartialSize() {
            // when
            List<InProgressCycleResponse> actual = cycleApiService.findInProgressByMe(
                    tokenPayload, now, new PagingParams(null, 2, inProgress3.getId(), null));

            // then
            assertThat(actual)
                    .map(InProgressCycleResponse::getCycleId)
                    .containsExactly(proceed2.getId(), proceed1.getId());
        }

        @DisplayName("진행 중인 모든 사이클을 최대 페이지를 초과하여 조회")
        @Test
        void findAllInProgress_pagingOverMaxPage() {
            // when
            List<InProgressCycleResponse> actual = cycleApiService.findInProgressByMe(
                    tokenPayload, now, new PagingParams(null, 3, proceed1.getId(), null));

            // then
            assertThat(actual).isEmpty();
        }

        @DisplayName("모든 사이클 갯수와 성공 사이클 갯수 조회")
        @Test
        void searchStat() {
            // when
            StatResponse response = cycleApiService.searchStat(tokenPayload);

            // then
            assertAll(
                    () -> assertThat(response.getTotalCount()).isEqualTo(7),
                    () -> assertThat(response.getSuccessCount()).isEqualTo(1)
            );
        }
    }

    @DisplayName("자신의 특정 챌린지를 기준으로")
    @Nested
    class Nested2 {
        Cycle inProgress1;
        Cycle failed1;
        Cycle failed2;
        Cycle success1;
        Cycle success2;
        TokenPayload tokenPayload;

        @BeforeEach
        void setUp() {
            success1 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(6L));
            success2 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(9L));
            inProgress1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now);
            failed1 = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
            failed2 = fixture.사이클_생성_SECOND(조조그린_ID, 알고리즘_풀기_ID, now.minusDays(4L));
            tokenPayload = new TokenPayload(조조그린_ID);
        }

        @DisplayName("전체 사이클 조회")
        @Test
        void findAllWithChallenge() {
            //given
            PagingParams pagingParams = new PagingParams("startTime", null, 0L, null);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleApiService.findAllByMemberAndChallenge(
                    tokenPayload, 스모디_방문하기_ID, pagingParams);

            // then
            assertAll(
                    () -> assertThat(historyResponses).hasSize(4),
                    () -> assertThat(historyResponses)
                            .map(FilteredCycleHistoryResponse::getCycleId)
                            .containsExactly(inProgress1.getId(), failed1.getId(), success1.getId(), success2.getId()),
                    () -> assertThat(historyResponses)
                            .map(historyResponse -> historyResponse.getCycleDetails().size())
                            .containsExactly(1, 0, 3, 3)
            );
        }

        @DisplayName("전체 사이클 커서 페이징 조회")
        @Test
        void findAllWithChallenge_cursorPagination() {
            //given
            PagingParams pagingParams = new PagingParams("startTime", 2, failed1.getId(), null);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleApiService.findAllByMemberAndChallenge(
                    tokenPayload, 스모디_방문하기_ID, pagingParams);

            // then
            assertAll(
                    () -> assertThat(historyResponses).hasSize(2),
                    () -> assertThat(historyResponses)
                            .map(FilteredCycleHistoryResponse::getCycleId)
                            .containsExactly(success1.getId(), success2.getId()),
                    () -> assertThat(historyResponses)
                            .map(historyResponse -> historyResponse.getCycleDetails().size())
                            .containsExactly(3, 3)
            );
        }

        @DisplayName("성공 사이클 조회")
        @Test
        void findAllWithChallenge_success() {
            //given
            PagingParams pagingParams = new PagingParams("startTime", null, 0L, "success");

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleApiService.findAllByMemberAndChallenge(
                    tokenPayload, 스모디_방문하기_ID, pagingParams);

            // then
            assertAll(
                    () -> assertThat(historyResponses).hasSize(2),
                    () -> assertThat(historyResponses)
                            .map(FilteredCycleHistoryResponse::getCycleId)
                            .containsExactly(success1.getId(), success2.getId()),
                    () -> assertThat(historyResponses)
                            .map(historyResponse -> historyResponse.getCycleDetails().size())
                            .containsExactly(3, 3)
            );
        }

        @DisplayName("성공 사이클 커서 페이징 조회")
        @Test
        void findAllWithChallenge_successCursorPagination() {
            //given
            PagingParams pagingParams = new PagingParams("startTime", 1, success1.getId(), "success");

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleApiService.findAllByMemberAndChallenge(
                    tokenPayload, 스모디_방문하기_ID, pagingParams);

            // then
            assertAll(
                    () -> assertThat(historyResponses).hasSize(1),
                    () -> assertThat(historyResponses)
                            .map(FilteredCycleHistoryResponse::getCycleId)
                            .containsExactly(success2.getId()),
                    () -> assertThat(historyResponses)
                            .map(historyResponse -> historyResponse.getCycleDetails().size())
                            .containsExactly(3)
            );
        }

        @DisplayName("성공한 사이클이 없을 때 성공 기준 사이클 커서 페이징 조회")
        @Test
        void findAllWithChallenge_noSuccessCursorPagination() {
            //given
            PagingParams pagingParams = new PagingParams("startTime", null, 0L, "success");

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleApiService.findAllByMemberAndChallenge(
                    tokenPayload, 알고리즘_풀기_ID, pagingParams);

            // then
            assertThat(historyResponses).isEmpty();
        }
    }
}
