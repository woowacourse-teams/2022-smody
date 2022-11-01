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

    @Query("select count(r) from Record r "
            + "where r.deadLineTime >= :startTime and r.isSuccess=false ")
    Long countChallengers(@Param("startTime") LocalDateTime startTime);

    @Query("select " +
            "new com.woowacourse.smody.record.dto.ChallengersResult(r.challenge.id, count(r.member.id)) from Record r " +
            "where r.challenge in :challenges and r.deadLineTime >= :startTime and r.isSuccess=false " +
            "group by r.challenge")
    List<ChallengersResult> countChallengers(@Param("challenges") List<Challenge> challenges,
                                             @Param("startTime") LocalDateTime startTime);

    @Query("select " +
            "new com.woowacourse.smody.record.dto.InProgressResult(r.challenge.id, count(r.member.id)) from Record r " +
            "where r.member=:member and r.challenge in :challenges and r.deadLineTime >= :startTime and r.isSuccess=false " +
            "group by r.challenge")
    List<InProgressResult> isInProgress(@Param("member") Member member, @Param("challenges") List<Challenge> challenges,
                                        @Param("startTime") LocalDateTime startTime);
}
