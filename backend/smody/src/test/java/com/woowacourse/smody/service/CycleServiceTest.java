package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.ResourceFixture.알파_ID;
import static com.woowacourse.smody.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.FilteredCycleHistoryRequest;
import com.woowacourse.smody.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.dto.InProgressCycleResponse;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.StatResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.ImgBBImageStrategy;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class CycleServiceTest extends IntegrationTest {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private CycleQueryService cycleQueryService;

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
        Long cycleId = cycleService.create(
                new TokenPayload(조조그린_ID),
                new CycleRequest(time, 스모디_방문하기_ID)
        );
        CycleResponse cycleResponse = cycleQueryService.findById(cycleId);

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(cycleId),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(스모디_방문하기_ID),
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(time)
        );
    }

    @DisplayName("동일한 첼린지에 진행중인 사이클이 존재하는 경우 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_duplicateInProgressChallenge() {
        // given
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when then
        assertThatThrownBy(() -> cycleService.create(
                new TokenPayload(조조그린_ID),
                new CycleRequest(LocalDateTime.now(), 스모디_방문하기_ID)
        )).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
    }

    @DisplayName("현재 시각 기준 24시간이 이후가 지난 시작시간을 가진 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_overOneDay() {
        // given
        LocalDateTime invalidTime = LocalDateTime.now().plusDays(1L).plusSeconds(1L);

        // when then
        assertThatThrownBy(() -> cycleService.create(
                new TokenPayload(조조그린_ID),
                new CycleRequest(invalidTime, 알고리즘_풀기_ID)
        )).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_START_TIME);
    }

    @DisplayName("오늘 성공한 챌린지로 다시 사이클을 생성한 경우 사이클을 내일 날짜로 생성한다.")
    @Test
    void create_alreadySuccessChallenge() {
        // given
        Cycle success = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(2L));
        fixture.사이클_생성_SECOND(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));

        // when
        Long cycleId = cycleService.create(
                new TokenPayload(조조그린_ID),
                new CycleRequest(now, 스모디_방문하기_ID)
        );
        CycleResponse cycleResponse = cycleQueryService.findById(cycleId);

        // then
        assertAll(
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(success.getStartTime().plusDays(3L)),
                () -> assertThat(cycleResponse.getProgressCount()).isEqualTo(0)
        );
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
        ProgressResponse progressResponse = cycleService.increaseProgress(tokenPayload, request);

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
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        ProgressRequest request = new ProgressRequest(cycle.getId(), invalidTime, imageFile, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, request))
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
    void increaseProgress_twoTimeInOneDay(Progress progress, LocalDateTime progressTime, LocalDateTime invalidTime) {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        ProgressRequest request = new ProgressRequest(cycle.getId(), progressTime, imageFile, "인증 완료");
        cycleService.increaseProgress(tokenPayload, request);

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("진행도를 증가 시킬 때 사이클을 찾지 못했을 경우 예외 발생")
    @Test
    void increaseProgress_notExistCycle() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        ProgressRequest request = new ProgressRequest(1000L, LocalDateTime.now(), imageFile, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, request))
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
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        ProgressRequest request = new ProgressRequest(cycle.getId(), now.plusSeconds(1), imageFile, "인증 완료");

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, request))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.UNAUTHORIZED_MEMBER);
    }

    @DisplayName("진행 중인 자신의 모든 사이클을 조회")
    @Test
    void findAllInProgressOfMine() {
        // given
        Cycle inProgress1 = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        Cycle inProgress2 = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));
        Cycle future = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.plusSeconds(1L));

        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);

        // when
        List<InProgressCycleResponse> actual = cycleQueryService.findInProgressOfMine(
                tokenPayload, now, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual)
                        .map(InProgressCycleResponse::getCycleId)
                        .containsAll(List.of(inProgress1.getId(), inProgress2.getId(), future.getId())),
                () -> assertThat(actual)
                        .filteredOn(response -> response.getChallengeId().equals(1L))
                        .map(InProgressCycleResponse::getSuccessCount)
                        .containsExactly(1, 1),
                () -> assertThat(actual)
                        .filteredOn(response -> response.getChallengeId().equals(2L))
                        .map(InProgressCycleResponse::getSuccessCount)
                        .containsExactly(2)
        );
    }

    @DisplayName("id로 사이클 조회 시 성공")
    @Test
    void findById() {
        // given
        Cycle inProgress = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);
        Image progressImage = new Image(
                new MockMultipartFile("progressImage", "image".getBytes()),
                image -> "fakeUrl"
        );
        inProgress.increaseProgress(now.plusSeconds(1L), progressImage, "인증 완료");
        inProgress.increaseProgress(now.plusDays(1L).plusSeconds(1L), progressImage, "인증 완료");

        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_SECOND(조조그린_ID, 스모디_방문하기_ID, now.minusDays(6L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(9L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(12L));

        // when
        CycleResponse cycleResponse = cycleQueryService.findById(inProgress.getId());

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(inProgress.getId()),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(inProgress.getChallenge().getId()),
                () -> assertThat(cycleResponse.getChallengeName()).isEqualTo(inProgress.getChallenge().getName()),
                () -> assertThat(cycleResponse.getProgressCount()).isEqualTo(inProgress.getProgress().getCount()),
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(inProgress.getStartTime()),
                () -> assertThat(cycleResponse.getSuccessCount()).isEqualTo(2),
                () -> assertThat(cycleResponse.getCycleDetails().get(0).getProgressTime()).isEqualTo(
                        now.plusSeconds(1L)),
                () -> assertThat(cycleResponse.getCycleDetails().get(1).getProgressTime()).isEqualTo(
                        now.plusDays(1L).plusSeconds(1L))

        );
    }

    @DisplayName("미래 시점의 사이클은 현재 시점으로 인증 불가")
    @Test
    void progress_future_time() {
        // given
        Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.plusSeconds(1L));
        Image progressImage = new Image(
                new MockMultipartFile("progressImage", "image".getBytes()), new ImgBBImageStrategy()
        );

        // when then
        assertThatThrownBy(() -> cycle.increaseProgress(now, progressImage, "인증 완료"))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
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
            List<InProgressCycleResponse> actual = cycleQueryService.findInProgressOfMine(
                    tokenPayload, now, PageRequest.of(0, 10));

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
            List<InProgressCycleResponse> actual = cycleQueryService.findInProgressOfMine(
                    tokenPayload, now, PageRequest.of(0, 3));

            // then
            assertThat(actual)
                    .map(InProgressCycleResponse::getCycleId)
                    .containsExactly(inProgress1.getId(), inProgress2.getId(), inProgress3.getId());
        }

        @DisplayName("진행 중인 모든 사이클을 1페이지부터 2개만 조회")
        @Test
        void findAllInProgress_pagingPartialSize() {
            // when
            List<InProgressCycleResponse> actual = cycleQueryService.findInProgressOfMine(
                    tokenPayload, now, PageRequest.of(1, 3));

            // then
            assertThat(actual)
                    .map(InProgressCycleResponse::getCycleId)
                    .containsExactly(proceed2.getId(), proceed1.getId());
        }

        @DisplayName("진행 중인 모든 사이클을 최대 페이지를 초과하여 조회")
        @Test
        void findAllInProgress_pagingOverMaxPage() {
            // when
            List<InProgressCycleResponse> actual = cycleQueryService.findInProgressOfMine(
                    tokenPayload, now, PageRequest.of(2, 3));

            // then
            assertThat(actual).isEmpty();
        }

        @DisplayName("모든 사이클 갯수와 성공 사이클 갯수 조회")
        @Test
        void searchStat() {
            // when
            StatResponse response = cycleQueryService.searchStat(tokenPayload);

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
            inProgress1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now);
            failed1 = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
            failed2 = fixture.사이클_생성_SECOND(조조그린_ID, 알고리즘_풀기_ID, now.minusDays(4L));
            success1 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(6L));
            success2 = fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(9L));
            tokenPayload = new TokenPayload(조조그린_ID);
        }

        @DisplayName("전체 사이클 조회")
        @Test
        void findAllWithChallenge() {
            //given
            FilteredCycleHistoryRequest filteredCycleHistoryRequest = new FilteredCycleHistoryRequest(null, 10, null, 스모디_방문하기_ID);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleQueryService.findAllByMemberAndChallengeWithFilter(
                    tokenPayload, filteredCycleHistoryRequest);

            // then
            assertAll(
                    () -> assertThat(historyResponses.size()).isEqualTo(4),
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
        void findAllWithChallenge_cursorPaganation() {
            //given
            FilteredCycleHistoryRequest filteredCycleHistoryRequest = new FilteredCycleHistoryRequest(null, 10, failed1.getId(), 스모디_방문하기_ID);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleQueryService.findAllByMemberAndChallengeWithFilter(
                    tokenPayload, filteredCycleHistoryRequest);

            // then
            assertAll(
                    () -> assertThat(historyResponses.size()).isEqualTo(2),
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
        void findAllWithChallenge_succeess() {
            //given
            FilteredCycleHistoryRequest filteredCycleHistoryRequest = new FilteredCycleHistoryRequest("success", 10, null, 스모디_방문하기_ID);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleQueryService.findAllByMemberAndChallengeWithFilter(
                    tokenPayload, filteredCycleHistoryRequest);

            // then
            assertAll(
                    () -> assertThat(historyResponses.size()).isEqualTo(2),
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
        void findAllWithChallenge_successCursorPaganation() {
            //given
            FilteredCycleHistoryRequest filteredCycleHistoryRequest = new FilteredCycleHistoryRequest("success", 10, success1.getId(), 스모디_방문하기_ID);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleQueryService.findAllByMemberAndChallengeWithFilter(
                    tokenPayload, filteredCycleHistoryRequest);

            // then
            assertAll(
                    () -> assertThat(historyResponses.size()).isEqualTo(1),
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
        void findAllWithChallenge_noSuccessCursorPaganation() {
            //given
            FilteredCycleHistoryRequest filteredCycleHistoryRequest = new FilteredCycleHistoryRequest("success", 10, null, 알고리즘_풀기_ID);

            // when
            List<FilteredCycleHistoryResponse> historyResponses = cycleQueryService.findAllByMemberAndChallengeWithFilter(
                    tokenPayload, filteredCycleHistoryRequest);

            // then
            assertThat(historyResponses).isEmpty();
        }
    }
}
