package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
