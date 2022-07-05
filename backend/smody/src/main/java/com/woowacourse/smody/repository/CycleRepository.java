package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
}
