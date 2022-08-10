package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.CycleDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final CycleDetailRepository cycleDetailRepository;

    public CycleDetail search(Long cycleDetailId) {
        return cycleDetailRepository.findById(cycleDetailId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE_DETAIL));
    }
}
