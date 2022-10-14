package com.woowacourse.smody.feed.repository;

import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.feed.domain.Feed;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<CycleDetail, Long>, FeedDynamicRepository {

    @Query("select new com.woowacourse.smody.feed.domain.Feed("
            + "cd.id, cd.progressImage, cd.description, "
            + "cd.progressTime, cd.progress, "
            + "m.id, m.picture, m.nickname, "
            + "ch.id, ch.name, cd.comments.size) "
            + "from CycleDetail cd "
            + "join cd.cycle c "
            + "join c.challenge ch "
            + "join c.member m "
            + "where cd.id = :cycleDetailId ")
    Optional<Feed> findByCycleDetailId(@Param("cycleDetailId") Long cycleDetailId);
}
