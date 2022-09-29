package com.woowacourse.smody.ranking.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.ranking.domain.RankManager;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
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
    private final RankingActivityRepository rankingActivityRepository;

    public List<RankingPeriodResponse> findAllPeriodLatest() {
        List<RankingPeriod> rankingPeriods = rankingPeriodRepository.findAllByOrderByStartDateDesc();
        return rankingPeriods.stream()
                .map(RankingPeriodResponse::new)
                .collect(Collectors.toList());
    }

    public List<RankingActivityResponse> findAllActivity(Long rankingPeriodId) {
        RankingPeriod rankingPeriod = search(rankingPeriodId);
        List<RankingActivity> activities = rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(rankingPeriod);
        RankManager rankManager = RankManager.rank(activities);
        return activities.stream()
                .map(activity -> new RankingActivityResponse(rankManager.getRanking(activity), activity))
                .collect(Collectors.toList());
    }

    private RankingPeriod search(Long rankingPeriodId) {
        return rankingPeriodRepository.findById(rankingPeriodId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_RANKING_PERIOD));
    }

    public RankingActivityResponse findActivityOfMine(TokenPayload payload, Long rankingPeriodId) {
        return findAllActivity(rankingPeriodId).stream()
                .filter(activity -> activity.getMemberId().equals(payload.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_RANKING_ACTIVITY));
    }
}
