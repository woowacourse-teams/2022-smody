package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    @EntityGraph(attributePaths = "challenge")
    List<Cycle> findAllByStartTimeIsAfter(LocalDateTime time);

    @EntityGraph(attributePaths = "challenge")
    List<Cycle> findAllByMemberAndStartTimeIsAfter(Member member, LocalDateTime time);

    Long countByMemberAndChallengeAndProgress(Member member, Challenge challenge, Progress progress);
}
