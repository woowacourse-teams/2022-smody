package com.woowacourse.smody.cycle.service;

import static com.woowacourse.smody.support.ResourceFixture.FORMATTER;
import static com.woowacourse.smody.support.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
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
import org.springframework.beans.factory.annotation.Autowired;

class CycleServiceTest extends IntegrationTest {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private ChallengeRepository challengeRepository;

    private final LocalDateTime now = LocalDateTime.now();

    @DisplayName("사이클을 생성한다.")
    @Test
    void create() {
        // when
        LocalDateTime time = LocalDateTime.of(1996, 8, 30, 8, 0);
        Cycle cycle = cycleService.create(조조그린_ID, 스모디_방문하기_ID, time);

        // then
        assertThat(cycle.getId()).isNotNull();
    }

    @DisplayName("동일한 첼린지에 진행중인 사이클이 존재하는 경우 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_duplicateInProgressChallenge() {
        // given
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when then
        assertThatThrownBy(() -> cycleService.create(조조그린_ID, 스모디_방문하기_ID, now))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
    }

    @DisplayName("현재 시각 기준 24시간이 이후가 지난 시작시간을 가진 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_overOneDay() {
        // given
        LocalDateTime invalidTime = LocalDateTime.now().plusDays(1L).plusSeconds(1L);

        // when then
        assertThatThrownBy(() -> cycleService.create(조조그린_ID, 스모디_방문하기_ID, invalidTime))
                .isInstanceOf(BusinessException.class)
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
        Cycle cycle = cycleService.create(조조그린_ID, 스모디_방문하기_ID, now);

        // then
        assertAll(
                () -> assertThat(cycle.getStartTime().format(FORMATTER))
                        .isEqualTo(success.getStartTime().plusDays(3L).format(FORMATTER)),
                () -> assertThat(cycle.getProgress()).isEqualTo(Progress.NOTHING)
        );
    }

    @DisplayName("미래 시점의 사이클은 현재 시점으로 인증 불가")
    @Test
    void progress_future_time() {
        // given
        Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.plusSeconds(1L));

        // when then
        assertThatThrownBy(() -> cycle.increaseProgress(now, 이미지, "인증 완료"))
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

        @DisplayName("챌린지들에 해당하는 진행 중인 모든 사이클을 조회")
        @Test
        void searchInProgressByChallenges() {
            List<Cycle> cycles = cycleService.findInProgressByChallenges(now, List.of(
                    challengeRepository.findById(스모디_방문하기_ID).get(),
                    challengeRepository.findById(미라클_모닝_ID).get())
            );

            assertThat(cycles).hasSize(2);
        }

        @DisplayName("챌린지에 해당하는 진행 중인 모든 사이클을 조회")
        @Test
        void searchInProgressByChallenge() {
            List<Cycle> cycles = cycleService.findInProgressByChallenge(
                    now, challengeRepository.findById(스모디_방문하기_ID).get()
            );

            assertThat(cycles).hasSize(1);
        }
    }
}
