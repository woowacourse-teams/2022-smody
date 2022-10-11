package com.woowacourse.smody.challenge.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.db_support.PagingParams;
import java.util.List;

public interface DynamicChallengeRepository {

    List<Challenge> findAllByFilter(PagingParams pagingParams);
}
