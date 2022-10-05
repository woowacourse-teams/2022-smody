package com.woowacourse.smody.cycle.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CycleRepository extends JpaRepository<Cycle, Long>, DynamicCycleRepository {

    List<Cycle> findAllByStartTimeIsAfter(LocalDateTime time);

    @Query("select c from Cycle c where c.startTime >= :time and c.challenge in :challenges")
    List<Cycle> findAllByStartTimeIsAfterAndChallengeIn(@Param("time") LocalDateTime time,
                                                        @Param("challenges") List<Challenge> challenges);

    @Query("select c from Cycle c where c.startTime >= :time and c.challenge = :challenge")
    List<Cycle> findAllByStartTimeIsAfterAndChallenge(@Param("time") LocalDateTime time,
                                                      @Param("challenge") Challenge challenge);

    @Query("select count(c) from Cycle c where c.member = :member and "
            + "c.challenge = :challenge and c.progress = 'SUCCESS'")
    Long countSuccess(@Param("member") Member member, @Param("challenge") Challenge challenge);

    @Query(value = "select * from cycle c where c.member_id = :memberId and c.challenge_id = :challengeId " +
            "order by c.start_time DESC limit 1",
            nativeQuery = true)
    Optional<Cycle> findRecent(@Param("memberId") Member member, @Param("challengeId") Challenge challenge);

    List<Cycle> findByMember(Member member);

    @Modifying
    @Query("delete from Cycle c where c.member = :member")
    void deleteByMember(@Param("member") Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cycle c where c.id = :id")
    Optional<Cycle> findByIdWithLock(@Param("id") Long id);

    List<Cycle> findAllByChallengeIdAndMemberId(Long challengeId, Long memberId);
}
