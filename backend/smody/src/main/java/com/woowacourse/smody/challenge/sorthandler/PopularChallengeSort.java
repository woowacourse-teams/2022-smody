package com.woowacourse.smody.challenge.sorthandler;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.db_support.CursorPaging;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopularChallengeSort implements ChallengeSort {

    private static final String SORT_VALUE = "popular";

    private final CycleService cycleService;

    @Override
    public List<Challenge> getSortedChallenges(LocalDateTime searchTime, PagingParams pagingParams,
                                               ChallengingRecords challengingRecords) {
        List<Cycle> cycles = cycleService.findInProgress(searchTime);
        challengingRecords.record(cycles);
        List<Challenge> challenges = challengingRecords.getChallengesOrderByChallenger();
        return CursorPaging.apply(challenges, null, pagingParams.getSize());
    }

    @Override
    public String getSortValue() {
        return SORT_VALUE;
    }
}
