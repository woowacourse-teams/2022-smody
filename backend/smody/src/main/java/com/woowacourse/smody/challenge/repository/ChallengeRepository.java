package com.woowacourse.smody.challenge.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, DynamicChallengeRepository {

    Optional<Challenge> findByName(String name);
}
