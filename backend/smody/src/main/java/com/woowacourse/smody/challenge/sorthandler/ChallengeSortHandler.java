package com.woowacourse.smody.challenge.sorthandler;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeSortHandler {

    List<Challenge> handle(LocalDateTime searchTime, PagingParams pagingParams,
                           ChallengingRecords challengingRecords);

    String getSortValue();
}
