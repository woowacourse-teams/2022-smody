package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Progress {

    NOTHING(0) {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 1L)) {
                return FIRST;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }

        @Override
        public boolean isInProgress(LocalDateTime startTime, LocalDateTime now) {
            LocalDateTime toTime = startTime.plusDays(1L);
            return now.isBefore(toTime);
        }

        @Override
        public long calculateDeadLineToMillis(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(1L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }
    },
    FIRST(1) {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 2L)) {
                return SECOND;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }

        @Override
        public boolean isInProgress(LocalDateTime startTime, LocalDateTime now) {
            LocalDateTime toTime = startTime.plusDays(2L);
            return now.isBefore(toTime);
        }

        @Override
        public long calculateDeadLineToMillis(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(2L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }
    },
    SECOND(2) {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 3L)) {
                return SUCCESS;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }

        @Override
        public boolean isInProgress(LocalDateTime startTime, LocalDateTime now) {
            LocalDateTime toTime = startTime.plusDays(3L);
            return now.isBefore(toTime);
        }

        @Override
        public long calculateDeadLineToMillis(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(3L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }
    },
    SUCCESS(3) {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            throw new BusinessException(ExceptionData.ALREADY_SUCCESS);
        }

        @Override
        public boolean isInProgress(LocalDateTime startTime, LocalDateTime now) {
            return false;
        }

        @Override
        public long calculateDeadLineToMillis(LocalDateTime startTime, LocalDateTime nowTime) {
            return -1L * Long.MAX_VALUE;
        }
    };

    private final int count;

    public abstract Progress increase(LocalDateTime startTime, LocalDateTime progressTime);

    public abstract boolean isInProgress(LocalDateTime startTime, LocalDateTime now);

    public abstract long calculateDeadLineToMillis(LocalDateTime startTime, LocalDateTime testTime);

    public static Optional<Progress> from(String name) {
        return Arrays.stream(values())
                .filter(progress -> progress.name().equalsIgnoreCase(name))
                .findAny();
    }

    private static boolean isBetween(LocalDateTime startTime, LocalDateTime progressTime, Long interval) {
        LocalDateTime fromTime = startTime.plusDays(interval - 1L);
        LocalDateTime toTime = startTime.plusDays(interval);
        return (progressTime.isEqual(fromTime) || progressTime.isAfter(fromTime))
                && progressTime.isBefore(toTime);
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
