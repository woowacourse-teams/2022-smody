package com.woowacourse.smody.feed.service;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.feed.dto.FeedResponse;
import com.woowacourse.smody.feed.repository.FeedRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedApiService {

    private final FeedService feedService;

    public List<FeedResponse> findAll(PagingParams pagingParams) {
        List<Feed> feeds = feedService.searchAll(pagingParams);
        return feeds.stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }

    public FeedResponse searchById(Long cycleDetailId) {
        Feed feed = feedService.search(cycleDetailId);
        return new FeedResponse(feed);
    }
}