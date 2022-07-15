package com.woowacourse.smody.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CycleTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    private static final Cycle.CycleBuilder cycleBuilder = Cycle.builder()
            .member(new Member(EMAIL, NICKNAME, PICTURE))
            .challenge(new Challenge("미라클 모닝"));

    @DisplayName("유효한 시간일때 사이클의 진행도를 증가시킨다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:00,FIRST",
            "NOTHING,2022-01-01T23:59:59,FIRST",
            "FIRST,2022-01-02T00:00:00,SECOND",
            "FIRST,2022-01-02T23:59:59,SECOND",
            "SECOND,2022-01-03T00:00:00,SUCCESS",
            "SECOND,2022-01-03T23:59:59,SUCCESS"
    })
    void increaseProgress(Progress progress, LocalDateTime progressTime, Progress expected) {
        // given
        Cycle cycle = cycleBuilder
                .progress(progress)
                .startTime(LocalDateTime.of(2022, 1, 1, 0, 0))
                .build();

        // when
        cycle.increaseProgress(progressTime);

        // then
        assertThat(cycle.getProgress()).isEqualTo(expected);
    }

    @DisplayName("유효한 시간이 아닐 때 사이클의 진행도를 증가시킬 때 예외를 발생시킨다.")
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
        Cycle cycle = cycleBuilder
                .progress(progress)
                .startTime(LocalDateTime.of(2022, 1, 1, 0, 0))
                .build();

        // when then
        assertThatThrownBy(() -> cycle.increaseProgress(invalidTime))
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
        Cycle cycle = cycleBuilder
                .progress(progress)
                .startTime(LocalDateTime.of(2022, 1, 1, 0, 0))
                .build();

        cycle.increaseProgress(progressTime);

        // when then
        assertThatThrownBy(() -> cycle.increaseProgress(invalidTime))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("성공한 사이클의 진행도를 증가시키면 예외 발생시킨다")
    @Test
    void increaseProgress_alreadySuccess() {
        // given
        Cycle cycle = cycleBuilder
                .progress(Progress.SUCCESS)
                .startTime(LocalDateTime.now())
                .build();

        // when then
        assertThatThrownBy(() -> cycle.increaseProgress(LocalDateTime.now()))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.ALREADY_SUCCESS);
    }

    @DisplayName("진행중인 사이클을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:01",
            "FIRST,2022-01-01T00:00:01",
            "FIRST,2022-01-02T00:00:01",
            "SECOND,2022-01-01T00:00:01",
            "SECOND,2022-01-02T00:00:01",
            "SECOND,2022-01-03T00:00:01"
    })
    void isInProgress(Progress progress, LocalDateTime now) {
        // given
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 0, 0);
        Cycle cycle = cycleBuilder.progress(progress)
                .startTime(startTime)
                .build();

        // when then
        assertThat(cycle.isInProgress(now)).isTrue();
    }

    @DisplayName("미래 시점의 사이클은 인증된 상태일 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"SUCCESS", "SECOND", "FIRST"})
    void new_cannotMakeFuture(Progress progress) {
        // given
        Member member = new Member(EMAIL, NICKNAME, PICTURE);
        Challenge challenge = new Challenge("미라클 모닝");

        // when then
        assertThatThrownBy(() -> new Cycle(member, challenge, progress, LocalDateTime.now().plusSeconds(1L)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_START_TIME);
    }

    @DisplayName("사이클을 남은 인증 시간 별 오름차순으로 정렬한다.")
    @Test
    void sort_cycle() {
        // given
        Member member = new Member(EMAIL, NICKNAME, PICTURE);
        Challenge challenge = new Challenge("미라클 모닝");
        Cycle inProgress1 = new Cycle(member, challenge, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        List<Cycle> cycles = new ArrayList<>(
                List.of(inProgress1, inProgress2, inProgress3, proceed1, proceed2, success, failed));

        // when
        cycles.sort(Comparator.comparingLong(cycle -> cycle.calculateEndTime(LocalDateTime.now())));

        // then
        assertThat(cycles)
                .containsExactly(success, failed, inProgress1, inProgress2, inProgress3, proceed2, proceed1);
    }
}
