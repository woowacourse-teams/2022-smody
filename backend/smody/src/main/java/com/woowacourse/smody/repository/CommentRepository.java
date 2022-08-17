package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member", "cycleDetail"})
    List<Comment> findAllByCycleDetailId(Long cycleDetailId);
}
