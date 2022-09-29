package com.woowacourse.smody.ranking.service;

import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {

    private final RankingPeriodRepository rankingPeriodRepository;

    public List<RankingPeriodResponse> findAllPeriodLatest() {
        List<RankingPeriod> rankingPeriods = rankingPeriodRepository.findAllByOrderByStartDateDesc();
        return rankingPeriods.stream()
                .map(RankingPeriodResponse::new)
                .collect(Collectors.toList());
    }

    public List<RankingActivityResponse> findAllActivity(Long rankingPeriodId) {
        return null;
    }
}
