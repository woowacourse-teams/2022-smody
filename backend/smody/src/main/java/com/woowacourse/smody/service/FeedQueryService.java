package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.CycleDetailRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedQueryService {

    private final CycleDetailRepository cycleDetailRepository;

    public List<FeedResponse> searchAll(Integer size, Long cycleDetailId) {
        List<CycleDetail> cycleDetails = cycleDetailRepository.findAllWithCursorIdAndSize(cycleDetailId, size);
        return cycleDetails.stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }
}
