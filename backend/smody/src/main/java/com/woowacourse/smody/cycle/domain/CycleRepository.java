package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.repository.DynamicCycleRepository;
import com.woowacourse.smody.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CycleRepository extends JpaRepository<Cycle, Long>, DynamicCycleRepository {

    @Query("select c from Cycle c "
            + "join fetch c.challenge "
            + "where c.startTime > :startTime")
    List<Cycle> findAllByStartTimeIsAfter(@Param("startTime") LocalDateTime startTime);

    List<Cycle> findAllByStartTimeIsAfterAndChallengeIn(LocalDateTime startTime, List<Challenge> challenges);

    List<Cycle> findAllByStartTimeIsAfterAndChallenge(LocalDateTime startTime, Challenge challenge);

    @Query("select count(c) from Cycle c "
            + "where c.member = :member and c.challenge = :challenge and c.progress = 'SUCCESS'")
    Long countSuccess(@Param("member") Member member, @Param("challenge") Challenge challenge);

    @Query(value = "select * from cycle c "
            + "where c.member_id = :memberId and c.challenge_id = :challengeId " +
            "order by c.start_time DESC limit 1",
            nativeQuery = true)
    Optional<Cycle> findRecent(@Param("memberId") Long memberId, @Param("challengeId") Long challengeId);

    List<Cycle> findByMember(Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cycle c where c.id = :cycleId")
    Optional<Cycle> findByIdWithLock(@Param("cycleId") Long cycleId);

    @Query("select c from Cycle c "
            + "join fetch c.challenge join fetch c.member "
            + "where c.challenge.id = :challengeId and c.member.id = :memberId")
    List<Cycle> findAllByChallengeAndMember(@Param("challengeId") Long challengeId,
                                            @Param("memberId") Long memberId);
}
