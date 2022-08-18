package com.woowacourse.smody.challenge.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import java.util.List;

public interface DynamicChallengeRepository {

    List<Challenge> searchAll(String name, Long cursorId, Integer size);
}
