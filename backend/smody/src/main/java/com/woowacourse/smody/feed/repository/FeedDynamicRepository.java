package com.woowacourse.smody.feed.repository;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.domain.Feed;

import java.util.List;

public interface FeedDynamicRepository {

    List<Feed> searchAll(PagingParams pagingParams);
}
