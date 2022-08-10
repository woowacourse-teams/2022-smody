package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        public long calculateEndTime(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(1L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }

        @Override
        public boolean isIncreasePossible(LocalDateTime startTime, LocalDateTime progressTime) {
            return Progress.isBetween(startTime, progressTime, 1L);
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
        public long calculateEndTime(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(2L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }

        @Override
        public boolean isIncreasePossible(LocalDateTime startTime, LocalDateTime progressTime) {
            return Progress.isBetween(startTime, progressTime, 2L);
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
        public long calculateEndTime(LocalDateTime startTime, LocalDateTime nowTime) {
            LocalDateTime toTime = startTime.plusDays(3L);
            return ChronoUnit.MILLIS.between(nowTime, toTime);
        }

        @Override
        public boolean isIncreasePossible(LocalDateTime startTime, LocalDateTime progressTime) {
            return Progress.isBetween(startTime, progressTime, 3L);
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
        public long calculateEndTime(LocalDateTime startTime, LocalDateTime nowTime) {
            return -1L * Long.MAX_VALUE;
        }

        @Override
        public boolean isIncreasePossible(LocalDateTime startTime, LocalDateTime progressTime) {
            return false;
        }
    };

    private final int count;

    public abstract Progress increase(LocalDateTime startTime, LocalDateTime progressTime);

    public abstract boolean isInProgress(LocalDateTime startTime, LocalDateTime now);

    public abstract long calculateEndTime(LocalDateTime startTime, LocalDateTime testTime);

    abstract public boolean isIncreasePossible(LocalDateTime startTime, LocalDateTime progressTime);

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
