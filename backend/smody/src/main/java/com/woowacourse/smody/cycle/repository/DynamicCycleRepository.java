package com.woowacourse.smody.cycle.repository;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.cycle.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;

public interface DynamicCycleRepository {

    List<Cycle> findAllByMemberAndChallenge(Long memberId, Long challengeId, PagingParams pagingParams);

    List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams);

}
