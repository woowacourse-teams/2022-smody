package com.woowacourse.smody.ranking.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankManager;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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

    // 진행중인 랭킹 기간이 없으면 생성하는 로직 포함
    @Transactional
    public List<RankingPeriod> findInProgressPeriod(LocalDateTime time) {
        List<RankingPeriod> inProgressPeriods = rankingPeriodRepository.findAllByStartDateBefore(time).stream()
                .filter(period -> period.isBeforeEndTime(time))
                .collect(Collectors.toList());
        return checkInProgressWeekPeriod(time, inProgressPeriods);
    }

    private List<RankingPeriod> checkInProgressWeekPeriod(LocalDateTime time, List<RankingPeriod> inProgressPeriods) {
        if (inProgressPeriods.isEmpty()) {
            inProgressPeriods.add(rankingPeriodRepository.save(new RankingPeriod(getMonday(time), Duration.WEEK)));
        }
        return inProgressPeriods;
    }

    private LocalDateTime getMonday(LocalDateTime time) {
        return time.with(DayOfWeek.MONDAY)
                .toLocalDate()
                .atTime(0, 0, 0);
    }

    // 진행중인 랭킹 활동이 없으면 생성하는 로직 포함
    @Transactional
    public List<RankingActivity> findInProgressActivity(LocalDateTime time, Member member) {
        List<RankingPeriod> periods = findInProgressPeriod(time);
        List<RankingActivity> activities = rankingActivityRepository.findAllByMemberAndRankingPeriodIn(member, periods);
        List<RankingPeriod> myPeriods = activities.stream()
                .map(RankingActivity::getRankingPeriod)
                .collect(Collectors.toList());
        for (RankingPeriod period : periods) {
            addActivity(member, activities, myPeriods, period);
        }
        return activities;
    }

    private void addActivity(Member member, List<RankingActivity> activities,
                             List<RankingPeriod> myPeriods,
                           RankingPeriod period) {
        if (!myPeriods.contains(period)) {
            activities.add(rankingActivityRepository.save(RankingActivity.zero(member, period)));
        }
    }
}
