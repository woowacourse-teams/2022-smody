package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import java.util.List;

import com.woowacourse.smody.domain.PagingParams;
import org.springframework.data.domain.Pageable;

public interface DynamicCycleRepository {

    List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, PagingParams pagingParams);

    List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams);

}
