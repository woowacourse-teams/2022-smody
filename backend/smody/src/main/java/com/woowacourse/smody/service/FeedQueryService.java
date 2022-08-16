package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Feed;
import com.woowacourse.smody.dto.FeedRequest;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.FeedRepository;
import com.woowacourse.smody.util.TimeStrategy;
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

    private final FeedRepository feedRepository;
    private final FeedService feedService;
    private final TimeStrategy timeStrategy;

    public List<FeedResponse> findAll(FeedRequest feedRequest) {
        Pageable pageRequest = feedRequest.toPageRequest();

        if (feedRequest.getLastCycleDetailId() == null) {
            List<Feed> feeds = feedRepository.findAllLatest(0L, timeStrategy.now(), pageRequest);
            return convertFeedResponse(feeds);
        }

        CycleDetail cycleDetail = feedService.search(feedRequest.getLastCycleDetailId());
        List<Feed> feeds = feedRepository.findAllLatest(cycleDetail.getId(), cycleDetail.getProgressTime(),
                pageRequest);
        return convertFeedResponse(feeds);
    }

    private List<FeedResponse> convertFeedResponse(List<Feed> feeds) {
        return feeds.stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }

    public FeedResponse searchById(Long cycleDetailId) {
        CycleDetail cycleDetail = feedService.search(cycleDetailId);
        return new FeedResponse(cycleDetail);
    }
}
