package com.woowacourse.smody.record.repository;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.record.domain.Record;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.record.dto.ChallengersResult;
import com.woowacourse.smody.record.dto.InProgressResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findByMemberAndAndChallenge(Member member, Challenge challenge);

    @Query(value = "select r.challenge_id, count(r.record_id) from record r where (select r.record_id from record r " +
            "where r.challenge_id in :challenges and r.dead_line >= :startTime and r.is_success=false " +
            "order by r.record_id limit 1) <= r.record_id and r.challenge_id in :challenges and r.dead_line >= :startTime and r.is_success=false " +
            "group by r.challenge_id", nativeQuery = true)
    List<Long []> countChallengersMultipleChallenge(@Param("challenges") List<Challenge> challenges,
                                             @Param("startTime") LocalDateTime startTime);

    @Query(value = "select r.challenge from Record r where r.member = :member and r.challenge in :challenges and r.deadLine >= :startTime and r.isSuccess = false")
    List<Long> isInProgressMultipleChallenge(@Param("member") Member member, @Param("challenges") List<Challenge> challenges,
                                        @Param("startTime") LocalDateTime startTime);

    @Query(value = "select count(r.record_id) from record r where (select r.record_id from record r " +
            "where r.challenge_id = :challenge and r.dead_line >= :startTime and r.is_success=false " +
            "order by r.record_id limit 1) <= r.record_id and r.challenge_id = :challenge and r.dead_line >= :startTime and r.is_success=false ", nativeQuery = true)
    Long countChallengersSingleChallenge(@Param("challenge") Challenge challenge,
                                                    @Param("startTime") LocalDateTime startTime);

    @Query(value = "select r.challenge from Record r where r.member = :member and r.challenge = :challenge and r.deadLine >= :startTime and r.isSuccess = false")
    Long isInProgressSingleChallenge(@Param("member") Member member, @Param("challenge") Challenge challenge,
                                                         @Param("startTime") LocalDateTime startTime);

    @Query(value = "select r.challenge, count(r.member) from Record r where r.challenge in :challenges and r.deadLine >= :startTime and r.isSuccess = false group by r.challenge")
    List<Long[]> countChallengersMultipleChallengeNaive(@Param("challenges") List<Challenge> challenges,
                                     @Param("startTime") LocalDateTime startTime);
}