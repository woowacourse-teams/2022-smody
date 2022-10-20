package com.woowacourse.smody.ranking.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.ranking.domain.RankManager;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingApiService {

    private final RankingService rankingService;

    public List<RankingPeriodResponse> findAllRankingPeriod(PagingParams pagingParams) {
        List<RankingPeriod> rankingPeriods = rankingService.findAllRankingPeriod(pagingParams);
        return rankingPeriods.stream()
                .map(RankingPeriodResponse::new)
                .collect(Collectors.toList());
    }

    public List<RankingActivityResponse> findAllRankingActivityByPeriodId(Long rankingPeriodId) {
        List<RankingActivity> rankingActivities = rankingService.findAllRankingActivityByPeriodId(rankingPeriodId);
        RankManager rankManager = RankManager.of(rankingActivities);
        return rankingActivities.stream()
                .map(activity -> new RankingActivityResponse(rankManager.getRanking(activity), activity))
                .collect(Collectors.toList());
    }

    public RankingActivityResponse findActivityOfMine(TokenPayload tokenPayload, Long rankingPeriodId) {
        List<RankingActivityResponse> responses = findAllRankingActivityByPeriodId(rankingPeriodId);
        return responses.stream()
                .filter(response -> response.getMemberId().equals(tokenPayload.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_RANKING_ACTIVITY));
    }
}
