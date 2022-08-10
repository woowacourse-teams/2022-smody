package com.woowacourse.smody.repository;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Progress;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DataJpaTest(showSql = false)
@Import(ResourceFixture.class)
public class CycleDetailRepositoryTest {

    @Autowired
    private CycleDetailRepository cycleDetailRepository;

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

    @DisplayName("첫 번째 데이터부터 개수만큼 데이터를 가져온다")
    @Test
    void findFirstById() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, today);
        CycleDetail 인증1 = cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(1L),
                "image.jpg", "인증1"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(2L),
                "image.jpg", "인증2"));
        cycleDetailRepository.save(new CycleDetail(cycle, today.plusMinutes(3L),
                "image.jpg", "인증3"));

        // when
        List<CycleDetail> cycleDetails = cycleDetailRepository.findAllLatest(0L, 3);

        // then
        assertThat(cycleDetails).hasSize(3);
    }

    @DisplayName("최신순으로 cycleDetail을 조회한다.")
    @Test
    void findAllLatest() {
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
        List<CycleDetail> cycleDetails = cycleDetailRepository.findAllLatest(인증5.getId(), 인증5.getProgressTime(), 3);

        // then
        assertAll(
                () -> assertThat(cycleDetails.size()).isEqualTo(3),
                () -> assertThat(cycleDetails.stream().map(CycleDetail::getDescription)).containsExactly(
                        "인증4", "인증3", "인증2"
                )
        );
    }
}
