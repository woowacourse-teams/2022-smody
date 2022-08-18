package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Feed;
import com.woowacourse.smody.domain.PagingParams;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.FeedRepository;
import com.woowacourse.smody.repository.SortSelection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedQueryService {

    private final FeedRepository feedRepository;
    private final FeedService feedService;

    public Pageable toPageRequest(Integer size, String sort) {
        if (size == null || size < 1) {
            return PageRequest.of(0, 10, SortSelection.findByParameter(sort).getSort());
        }
        return PageRequest.of(0, size, SortSelection.findByParameter(sort).getSort());
    }

    public List<FeedResponse> findAll(PagingParams pagingParams) {
        Pageable pageRequest = toPageRequest(pagingParams.getSize(), pagingParams.getSort());
        Long cursorId = pagingParams.getDefaultCursorId();
        Optional<Feed> findFeed = feedRepository.findByCycleDetailId(cursorId);

        if (findFeed.isEmpty()) {
            List<Feed> feeds = feedRepository.findAll(0L, LocalDateTime.now(), pageRequest);
            return convertFeedResponse(feeds);
        }

        Feed feed = findFeed.get();
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
