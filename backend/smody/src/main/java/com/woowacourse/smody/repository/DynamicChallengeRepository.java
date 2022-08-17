package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PagingParams;

import java.util.List;

public interface DynamicChallengeRepository {

    List<Challenge> searchAll(String name, Long cursorId, Integer size);
}
