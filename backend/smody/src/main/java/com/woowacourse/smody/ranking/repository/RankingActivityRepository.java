package com.woowacourse.smody.ranking.repository;

import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RankingActivityRepository extends JpaRepository<RankingActivity, Long> {

    @Query("select ra from RankingActivity ra "
            + "join fetch ra.member "
            + "where ra.rankingPeriod = :rankingPeriod "
            + "order by ra.point desc")
    List<RankingActivity> findAllByRankingPeriodOrderByPointDesc(@Param("rankingPeriod") RankingPeriod rankingPeriod);

    List<RankingActivity> findAllByMemberAndRankingPeriodIn(Member member, List<RankingPeriod> rankingPeriods);
}
