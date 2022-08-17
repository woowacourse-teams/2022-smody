package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;

import java.util.List;

public interface DynamicChallengeRepository {

    List<Challenge> searchAll(String name, Long cursorId, Integer size);
}
