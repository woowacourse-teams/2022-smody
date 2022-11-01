package com.woowacourse.smody.record.dto;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InProgressResult {

    private Long challengeId;
    private boolean isInProgress;

    public InProgressResult(Long challengeId, Long inProgress) {
        validateInProgress(inProgress);
        this.challengeId = challengeId;
        if (inProgress == 1L) {
            this.isInProgress = true;
        }
        else {
            this.isInProgress = false;
        }
    }

    private void validateInProgress(Long InProgress) {
        if (InProgress > 1) {
            throw new BusinessException(ExceptionData.INVALID_INPROGRESS_NUMBER);
        }
    }
}
