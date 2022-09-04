package com.woowacourse.smody.challenge.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.MemberChallenge;
import com.woowacourse.smody.member.domain.Member;
import java.util.List;

public interface DynamicChallengeRepository {

    List<Challenge> searchAll(String name, Long cursorId, Integer size);
}
