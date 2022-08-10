package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.CycleDetail;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CycleDetailRepository extends JpaRepository<CycleDetail, Long> {

    @Query(
            value = "select * from cycle_detail where cycle_detail_id > :id limit :size",
            nativeQuery = true
    )
    List<CycleDetail> findAllLatest(@Param("id") Long id, @Param("size") Integer size);

    @Query(
            value = "select * from cycle_detail where progress_time <= :time and cycle_detail_id != :id "
                    + "order by progress_time DESC, cycle_detail_id limit :size",
            nativeQuery = true
    )
    List<CycleDetail> findAllLatest(@Param("id") Long id, @Param("time") LocalDateTime time,
                                    @Param("size") Integer size);
}
// 시간 이상, 해당 id 초과로 한다!
