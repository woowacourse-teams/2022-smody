package com.woowacourse.smody.ranking.service;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.db_support.SortSelection;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {

    private final RankingPeriodRepository rankingPeriodRepository;
    private final RankingActivityRepository rankingActivityRepository;

    public List<RankingPeriod> findAllRankingPeriod(PagingParams pagingParams) {
        Sort sort = SortSelection.findByParameter(pagingParams.getSort()).getSort();
        return rankingPeriodRepository.findAll(sort);
    }

    public List<RankingActivity> findAllRankingActivityByPeriodId(Long rankingPeriodId) {
        RankingPeriod rankingPeriod = search(rankingPeriodId);
        return rankingActivityRepository.findAllByRankingPeriodOrderByPointDesc(rankingPeriod);
    }

    private RankingPeriod search(Long rankingPeriodId) {
        return rankingPeriodRepository.findById(rankingPeriodId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_RANKING_PERIOD));
    }

    public List<RankingPeriod> findInProgressPeriod(LocalDateTime time) {
        return rankingPeriodRepository.findAllByStartDateBefore(time).stream()
                .filter(period -> period.isBeforeEndTime(time))
                .collect(Collectors.toList());
    }

    public List<RankingActivity> findAllActivity(List<RankingPeriod> periods, Member member) {
        return rankingActivityRepository.findAllByMemberAndRankingPeriodIn(member, periods);
    }

    @Transactional
    public RankingPeriod createWeeklyPeriod(LocalDateTime startDate) {
        return rankingPeriodRepository.save(new RankingPeriod(startDate, Duration.WEEK));
    }

    @Transactional
    public RankingActivity createFirstActivity(Member member, RankingPeriod period) {
        return rankingActivityRepository.save(RankingActivity.ofZeroPoint(member, period));
    }
}
