package com.woowacourse.smody.comment.repository;

import com.woowacourse.smody.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cm from Comment cm "
            + "join fetch cm.member join fetch cm.cycleDetail "
            + "where cm.cycleDetail.id = :cycleDetailId")
    List<Comment> findAllByCycleDetailId(@Param("cycleDetailId") Long cycleDetailId);
}
