package com.woowacourse.smody.challenge.sort;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeSort {

    List<Challenge> getSortedChallenges(LocalDateTime searchTime, PagingParams pagingParams,
                                        ChallengingRecords challengingRecords);
}
