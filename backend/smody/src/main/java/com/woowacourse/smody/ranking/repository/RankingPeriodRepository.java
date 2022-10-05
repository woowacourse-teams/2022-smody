package com.woowacourse.smody.ranking.repository;

import com.woowacourse.smody.ranking.domain.RankingPeriod;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingPeriodRepository extends JpaRepository<RankingPeriod, Long> {

    List<RankingPeriod> findAll(Sort sort);

    List<RankingPeriod> findAllByStartDateBefore(LocalDateTime startDate);
}
