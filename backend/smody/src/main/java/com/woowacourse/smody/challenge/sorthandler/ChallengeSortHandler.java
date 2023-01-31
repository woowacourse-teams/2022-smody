package com.woowacourse.smody.challenge.sorthandler;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeSortHandler {

    private final List<ChallengeSort> challengeSorts;
    private final DefaultChallengeSort defaultChallengeSortHandler;

    public List<Challenge> handle(LocalDateTime searchTime, PagingParams pagingParams,
                                  ChallengingRecords challengingRecords) {
        ChallengeSort challengeSort = findChallengeSort(pagingParams.getSort());
        return challengeSort.getSortedChallenges(searchTime, pagingParams, challengingRecords);
    }

    private ChallengeSort findChallengeSort(String pagingParamsSortValue) {
        return challengeSorts.stream()
                .filter(challengeSortHandler -> challengeSortHandler.getSortValue()
                        .equals(pagingParamsSortValue))
                .findAny()
                .orElse(defaultChallengeSortHandler);
    }
}
