package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Feed;
import com.woowacourse.smody.dto.FeedRequest;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.FeedRepository;
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

    public List<FeedResponse> findAll(FeedRequest feedRequest) {
        Pageable pageRequest = feedRequest.toPageRequest();

        if (feedRequest.getLastCycleDetailId() == null) {
            List<Feed> feeds = feedRepository.findAll(0L, LocalDateTime.now(), pageRequest);
            return convertFeedResponse(feeds);
        }

        Feed feed = feedService.search(feedRequest.getLastCycleDetailId());
        List<Feed> feeds = feedRepository.findAll(feed.getCycleDetailId(), feed.getProgressTime(),
                pageRequest);
        return convertFeedResponse(feeds);
    }

    private List<FeedResponse> convertFeedResponse(List<Feed> feeds) {
        return feeds.stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }

    public FeedResponse searchById(Long cycleDetailId) {
        Feed feed = feedService.search(cycleDetailId);
        return new FeedResponse(feed);
    }
}
