package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.CycleDetailRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedQueryService {

    private static final int DEFAULT_CYCLE_DETAIL_ID = 0;

    private final CycleDetailRepository cycleDetailRepository;
    private final FeedService feedService;

    public List<FeedResponse> findAll(Pageable pageable, Long cycleDetailId) {
        if (cycleDetailId == DEFAULT_CYCLE_DETAIL_ID) {
            List<CycleDetail> cycleDetails = cycleDetailRepository.findAllLatest(
                    cycleDetailId, LocalDateTime.now(), pageable
            );
            return convertFeedResponse(cycleDetails);
        }
        CycleDetail cycleDetail = feedService.search(cycleDetailId);
        List<CycleDetail> cycleDetails = cycleDetailRepository.findAllLatest(
                cycleDetail.getId(), cycleDetail.getProgressTime(), pageable
        );
        return convertFeedResponse(cycleDetails);
    }

    public FeedResponse searchById(Long cycleDetailId) {
        CycleDetail cycleDetail = feedService.search(cycleDetailId);
        return new FeedResponse(cycleDetail);
    }

    private List<FeedResponse> convertFeedResponse(List<CycleDetail> cycleDetails) {
        return cycleDetails.stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }
}
