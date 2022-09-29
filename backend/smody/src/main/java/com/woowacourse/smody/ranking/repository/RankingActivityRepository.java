package com.woowacourse.smody.ranking.repository;

import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingActivityRepository extends JpaRepository<RankingActivity, Long> {

    @EntityGraph(attributePaths = "member")
    List<RankingActivity> findAllByRankingPeriodOrderByPointDesc(RankingPeriod rankingPeriod);
}
