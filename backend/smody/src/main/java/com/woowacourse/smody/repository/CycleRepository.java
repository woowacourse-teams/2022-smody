package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    List<Cycle> findAllByStartTimeIsAfter(LocalDateTime time);

    @EntityGraph(attributePaths = "challenge")
    @Query("select c from Cycle c where c.member = :member and c.startTime >= :time")
    List<Cycle> findByMemberAfterTime(@Param("member") Member member, @Param("time") LocalDateTime time);

    @Query("select count(c) from Cycle c where c.member = :member and "
            + "c.challenge = :challenge and c.progress = 'SUCCESS'")
    Long countSuccess(@Param("member") Member member, @Param("challenge") Challenge challenge);

    Optional<Cycle> findTopByMemberAndChallengeOrderByStartTimeDesc(Member member, Challenge challenge);
}
