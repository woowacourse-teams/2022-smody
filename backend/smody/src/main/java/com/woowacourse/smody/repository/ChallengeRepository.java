package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, DynamicChallengeRepository {

    Optional<Challenge> findByName(String name);
}
