package com.woowacourse.smody.cycle.service;

import static com.woowacourse.smody.support.ResourceFixture.FORMATTER;
import static com.woowacourse.smody.support.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.record.domain.Record;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.record.repository.RecordRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private RecordRepository recordRepository;

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

    @DisplayName("특정 멤버의 특정 챌린지에 대한 성공한 사이클 개수를 조회한다.")
    @Test
    void countSuccess() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        // when then
        Member member = fixture.회원_조회(조조그린_ID);
        Challenge challenge = fixture.챌린지_조회(미라클_모닝_ID);
        assertThat(cycleService.countSuccess(member, challenge)).isEqualTo(2);
    }

    @DisplayName("특정 챌린지와 특정 멤버의 사이클을 모두 조회한다.")
    @Test
    void findAllByChallengeAndMember() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when
        List<Cycle> actual = cycleService.findAllByChallengeAndMember(미라클_모닝_ID, 조조그린_ID);

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(3),
                () -> assertThat((int) actual.stream()
                        .filter(c -> c.getProgress().equals(Progress.SUCCESS)).count())
                        .isEqualTo(2)
        );
    }

    @DisplayName("특정 멤버의 모든 사이클 중 특정 PROGRESS 에 해당하는 사이클만 조회한다.")
    @Test
    void findAllByMemberAndFilter() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(3L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when
        PagingParams pagingParams = new PagingParams();
        pagingParams.setFilter("SUCCESS");
        List<Cycle> actual = cycleService.findAllByMemberAndFilter(fixture.회원_조회(조조그린_ID), pagingParams);

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(3),
                () -> assertThat(actual.stream()
                        .filter(c -> c.getChallenge().getId().equals(미라클_모닝_ID)).count())
                        .isEqualTo(2),
                () -> assertThat(actual.stream()
                        .filter(c -> c.getChallenge().getId().equals(스모디_방문하기_ID)).count())
                        .isEqualTo(1)
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

    @DisplayName("특정 챌린지의 현재 진행중인 사이클을 조회한다.")
    @Test
    void findInProgressByChallenge() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(1L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when
        List<Cycle> actual = cycleService.findInProgressByChallenge(now, fixture.챌린지_조회(미라클_모닝_ID));

        // then
        assertThat(actual.size()).isEqualTo(2);
    }

    @DisplayName("특정 멤버의 특정 챌린지에 대한 사이클 중 특정 PROGRESS 에 해당하는 것들만 모두 조회한다.")
    @Test
    void findAllByMemberAndChallengeAndFilter() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(1L));
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);

        // when
        PagingParams pagingParams = new PagingParams();
        pagingParams.setFilter("SUCCESS");
        List<Cycle> actual = cycleService.findAllByMemberAndChallengeAndFilter(
                조조그린_ID, 미라클_모닝_ID, pagingParams
        );

        // then
        assertThat(actual.size()).isEqualTo(2);
    }

    @DisplayName("진행중인 사이클을 조회한다.")
    @Test
    void findInProgress() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now);

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now.minusDays(1).minusMinutes(1));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 오늘의_운동_ID, now.minusDays(3));

        // when
        List<Cycle> actual = cycleService.findInProgress(now);

        // then
        assertThat(actual).map(cycle -> cycle.getChallenge().getId())
                .containsExactly(미라클_모닝_ID, 스모디_방문하기_ID, 오늘의_운동_ID);
    }

    @DisplayName("진행중인 사이클을 조회할 때 챌린지도 같이 조회한다.")
    @Test
    void findInProgressWithChallenge() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now);

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now.minusDays(1).minusMinutes(1));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 오늘의_운동_ID, now.minusDays(3));

        // when
        List<Cycle> actual = cycleService.findInProgress(now);

        // then
        assertThat(actual).map(cycle -> cycle.getChallenge().getName())
                .containsExactly("미라클 모닝", "스모디 방문하기", "오늘의 운동");
    }

    @DisplayName("사이클 생성 시 레코드도 같이 만들어진다")
    @Test
    void createRecord() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        Cycle cycle = cycleService.create(조조그린_ID, 미라클_모닝_ID, now);
        Optional<Record> myRecord = recordRepository.findByMemberAndAndChallenge(cycle.getMember(), cycle.getChallenge());

        // then
        assertThat(myRecord.isPresent()).isTrue();
    }
}
