package com.woowacourse.smody.feed.service;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.feed.repository.FeedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public List<Feed> findAll(PagingParams pagingParams) {
        return feedRepository.findAll(pagingParams);
    }

    public Feed search(Long cycleDetailId) {
        return feedRepository.findByCycleDetailId(cycleDetailId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE_DETAIL));
    }
}
