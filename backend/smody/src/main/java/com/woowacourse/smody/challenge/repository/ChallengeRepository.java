package com.woowacourse.smody.challenge.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, DynamicChallengeRepository {

    Optional<Challenge> findByName(String name);

    @Query("select c.id from Challenge c")
    List<Long> findAllIds();

    List<Challenge> findAllByIdIn(List<Long> challengeIds);
}
