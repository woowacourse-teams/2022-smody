package com.woowacourse.smody.comment.repository;

import com.woowacourse.smody.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member", "cycleDetail"})
    List<Comment> findAllByCycleDetailId(Long cycleDetailId);
}
