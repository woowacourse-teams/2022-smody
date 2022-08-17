package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DynamicCycleRepository {

    List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, String filter, Long lastCycleId, Pageable pageable);

    List<Cycle> findByMemberWithFilter(Long memberId, String filter);
}
