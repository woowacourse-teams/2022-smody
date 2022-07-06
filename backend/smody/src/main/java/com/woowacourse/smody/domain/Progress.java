package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;

public enum Progress {

    NOTHING {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 1L)) {
                return FIRST;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }
    },
    FIRST {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 2L)) {
                return SECOND;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }
    },
    SECOND {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            if (isBetween(startTime, progressTime, 3L)) {
                return SUCCESS;
            }
            throw new BusinessException(ExceptionData.INVALID_PROGRESS_TIME);
        }
    },
    SUCCESS {
        @Override
        public Progress increase(LocalDateTime startTime, LocalDateTime progressTime) {
            throw new BusinessException(ExceptionData.ALREADY_SUCCESS);
        }
    };

    abstract public Progress increase(LocalDateTime startTime, LocalDateTime progressTime);

    private static boolean isBetween(LocalDateTime startTime, LocalDateTime progressTime, Long interval) {
        LocalDateTime fromTime = startTime.plusDays(interval - 1L);
        LocalDateTime toTime = startTime.plusDays(interval);
        return (progressTime.isEqual(fromTime) || progressTime.isAfter(fromTime))
                && progressTime.isBefore(toTime);
    }
}
