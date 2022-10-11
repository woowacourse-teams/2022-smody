package com.woowacourse.smody.cycle.repository;

import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;

public interface DynamicCycleRepository {

    List<Cycle> findAllByMemberAndChallenge(Long memberId, Long challengeId, PagingParams pagingParams);

    List<Cycle> findAllByMemberAndFilter(Long memberId, PagingParams pagingParams);

    List<ChallengingRecord> findAllChallengingRecordByMemberAfterTime(Long memberId, LocalDateTime time);
}
