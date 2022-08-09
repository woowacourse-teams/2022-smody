package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.CycleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CycleDetailRepository extends JpaRepository<CycleDetail, Long> {

    @Query(
            value = "select * from cycle_detail where cycle_detail_id > :id limit :size",
            nativeQuery = true
    )
    List<CycleDetail> findAllWithCursorIdAndSize(@Param("id") Long id, @Param("size") Integer size);
}
