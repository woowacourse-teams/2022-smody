package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
