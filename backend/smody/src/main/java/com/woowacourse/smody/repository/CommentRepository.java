package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member", "cycleDetail"})
    List<Comment> findAllByCycleDetailId(Long cycleDetailId);
}
