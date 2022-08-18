package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PagingParams;

import java.time.LocalDateTime;
import java.util.List;

public interface DynamicCycleRepository {

    List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, LocalDateTime lastTime, PagingParams pagingParams);

    List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams);

}
