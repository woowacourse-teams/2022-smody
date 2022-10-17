package com.woowacourse.smody.feed.repository;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.support.RepositoryTest;
import com.woowacourse.smody.support.ResourceFixture;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FeedRepositoryTest extends RepositoryTest {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ResourceFixture resourceFixture;

    @DisplayName("피드를 조회한다.")
    @Test
    void findAll() {
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(2);
        commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "댓글"));
        entityManager.clear();

        List<Feed> feeds = feedRepository.searchAll(new PagingParams("latest", 10, 0L));

        assertThat(feeds).hasSize(3);
    }

    @DisplayName("피드를 하나만 조회한다.")
    @Test
    void findByCycleDetailId() {
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(1);
        Long id = cycleDetail.getId();
        commentRepository.save(new Comment(cycleDetail, cycle.getMember(), "댓글"));
        entityManager.clear();

        Feed feed = feedRepository.findByCycleDetailId(id).get();
        assertThat(feed.getCycleDetailId()).isEqualTo(id);
        assertThat(feed.getProgress()).isEqualTo(Progress.SECOND);
    }
}
