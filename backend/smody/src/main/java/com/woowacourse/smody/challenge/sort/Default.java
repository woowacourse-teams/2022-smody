package com.woowacourse.smody.challenge.sort;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Default implements ChallengeSort {

    private final ChallengeService challengeService;
    private final CycleService cycleService;

    @Override
    public List<Challenge> getSortedChallenges(LocalDateTime searchTime, PagingParams pagingParams,
                                               ChallengingRecords challengingRecords) {
        List<Challenge> challenges = challengeService.findAllByFilter(pagingParams);
        List<Cycle> cycles = cycleService.findInProgressByChallenges(searchTime, challenges);
        challengingRecords.record(cycles);
        return challenges;
    }
}
