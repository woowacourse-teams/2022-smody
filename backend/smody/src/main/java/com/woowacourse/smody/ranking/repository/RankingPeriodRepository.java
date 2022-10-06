package com.woowacourse.smody.ranking.repository;

import com.woowacourse.smody.ranking.domain.RankingPeriod;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RankingPeriodRepository extends JpaRepository<RankingPeriod, Long> {

    List<RankingPeriod> findAll(Sort sort);

    List<RankingPeriod> findAllByStartDateBefore(LocalDateTime startDate);
}
