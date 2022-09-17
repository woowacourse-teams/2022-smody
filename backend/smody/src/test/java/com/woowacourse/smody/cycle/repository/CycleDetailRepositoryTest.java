package com.woowacourse.smody.cycle.repository;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.feed.domain.Feed;
import com.woowacourse.smody.feed.repository.FeedRepository;
import com.woowacourse.smody.support.RepositoryTest;
import com.woowacourse.smody.support.ResourceFixture;

class CycleDetailRepositoryTest extends RepositoryTest {

    @Autowired
    private FeedRepository cycleDetailRepository;

    @Autowired
    private ResourceFixture fixture;

    @DisplayName("Cycle을 DB에 저장 시 CycleDetail 리스트도 같이 저장된다")
    @Test
    void saveWithCycleDetails() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, today);
        // when
        cycle.increaseProgress(today.plusMinutes(5L),이미지, "인증");

        // then
        assertThat(cycleDetailRepository.findAll()).hasSize(1);
    }

    @DisplayName("최신순으로 Feed를 조회한다.")
    @Test
    void findAll() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, today);
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(1L),
                "image.jpg", "인증1"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(2L),
                "image.jpg", "인증2"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(3L),
                "image.jpg", "인증3"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(4L),
                "image.jpg", "인증4"));
        CycleDetail cycleDetail = cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(5L),
                "image.jpg", "인증5"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(6L),
                "image.jpg", "인증6"));

        // when
        List<Feed> feeds = cycleDetailRepository.searchAll(new PagingParams("latest", 3, cycleDetail.getId()));

        // then
        assertAll(
                () -> assertThat(feeds.size()).isEqualTo(3),
                () -> assertThat(feeds.stream().map(Feed::getDescription)).containsExactly(
                        "인증4", "인증3", "인증2"
                )
        );
    }
}
