package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    @EntityGraph(attributePaths = "challenge")
    List<Cycle> findAllByStartTimeIsAfter(LocalDateTime time);
}
