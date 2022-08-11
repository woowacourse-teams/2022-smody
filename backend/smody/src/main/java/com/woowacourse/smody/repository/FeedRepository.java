package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Feed;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<CycleDetail, Long> {

    String feedMapping = "new com.woowacourse.smody.domain.Feed("
            + "cd.id, cd.progressImage, cd.description, cd.progressTime, "
            + "m.id, m.picture, m.nickname, "
            + "ch.id, ch.name ) "
            + "from CycleDetail cd "
            + "join cd.cycle c "
            + "join c.challenge ch "
            + "join c.member m ";

    @Query("select " + feedMapping
            + "where cd.progressTime <= :time and cd.id <> :id ")
    List<Feed> findAllLatest(@Param("id") Long id, @Param("time") LocalDateTime time, Pageable pageable);
}
