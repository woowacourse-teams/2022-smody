package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface DynamicCycleRepository {

    List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, String filter, Long lastCycleId, Pageable pageable);
}
