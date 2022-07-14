package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
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

    @Query(value = "select * from cycle c where c.member_id = :memberId and c.challenge_id = :challengeId " +
            "order by c.start_time DESC limit 1",
            nativeQuery = true)
    Optional<Cycle> findRecent(@Param("memberId") Member member, @Param("challengeId") Challenge challenge);

    List<Cycle> findByMember(Member member);

    @EntityGraph(attributePaths = "challenge")
    @Query("select c from Cycle c where c.member = :member and c.progress = 'SUCCESS' " +
            "order by c.startTime DESC")
    List<Cycle> findAllSuccessLatest(Member member);
}
