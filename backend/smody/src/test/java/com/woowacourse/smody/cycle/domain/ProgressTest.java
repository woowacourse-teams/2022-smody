package com.woowacourse.smody.cycle.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProgressTest {

    private static final LocalDateTime START_TIME =
            LocalDateTime.of(2022, 1, 1, 0, 0, 0);

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
        // when then
        assertThat(progress.increase(START_TIME, progressTime)).isEqualTo(expected);
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
        // when then
        assertThatThrownBy(() -> progress.increase(START_TIME, invalidTime))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("성공한 사이클의 진행도를 증가시키면 예외 발생시킨다")
    @Test
    void increaseProgress_alreadySuccess() {
        // when then
        assertThatThrownBy(() -> Progress.SUCCESS.increase(START_TIME, LocalDateTime.now()))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.ALREADY_SUCCESS);
    }
}
