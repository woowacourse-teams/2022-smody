package com.woowacourse.smody.ranking.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingActivityRepository extends JpaRepository<RankingActivity, Long> {

    @EntityGraph(attributePaths = "member")
    List<RankingActivity> findAllByRankingPeriodOrderByPointDesc(RankingPeriod rankingPeriod);

    List<RankingActivity> findAllByMemberAndRankingPeriodIn(Member member, List<RankingPeriod> rankingPeriods);
}
