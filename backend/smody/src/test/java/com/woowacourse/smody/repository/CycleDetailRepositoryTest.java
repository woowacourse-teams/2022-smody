package com.woowacourse.smody.repository;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Feed;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Progress;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DataJpaTest(showSql = false)
@Import(ResourceFixture.class)
public class CycleDetailRepositoryTest {

    @Autowired
    private FeedRepository cycleDetailRepository;

    @Autowired
    private ResourceFixture fixture;

    @DisplayName("Cycle을 DB에 저장 시 CycleDetail 리스트도 같이 저장된다")
    @Test
    void saveWithCycleDetails() {
        // given
        LocalDateTime today = LocalDateTime.now();
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, today);
        // when
        cycle.increaseProgress(
                today.plusMinutes(5L), new Image(imageFile, image -> "progressImage.jpg"), "인증"
        );

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
        CycleDetail 인증5 = cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(5L),
                "image.jpg", "인증5"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(6L),
                "image.jpg", "인증6"));
        // when
        List<Feed> feeds = cycleDetailRepository.findAllLatest(인증5.getId(), 인증5.getProgressTime(),
                PageRequest.of(0, 3, SortSelection.FEED_LATEST.getSort()));

        // then
        assertAll(
                () -> assertThat(feeds.size()).isEqualTo(3),
                () -> assertThat(feeds.stream().map(Feed::getDescription)).containsExactly(
                        "인증4", "인증3", "인증2"
                )
        );
    }
}
