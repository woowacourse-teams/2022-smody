package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.CycleDetail;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CycleDetailRepository extends JpaRepository<CycleDetail, Long> {

    @EntityGraph(attributePaths = {"cycle", "cycle.challenge", "cycle.member"})
    @Query("select cd from CycleDetail cd where cd.progressTime <= :time and cd.id <> :id "
            + "order by cd.progressTime desc, cd.id desc")
    List<CycleDetail> findAllLatest(@Param("id") Long id, @Param("time") LocalDateTime time, Pageable pageable);
}
