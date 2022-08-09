package com.woowacourse.smody.repository;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Progress;
import java.time.LocalDateTime;
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
    private CycleRepository cycleRepository;

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
        Cycle cycle = new Cycle(fixture.회원_조회(조조그린_ID), fixture.챌린지_조회(미라클_모닝_ID), Progress.NOTHING,
                today);

        // when
        cycle.increaseProgress(
                today.plusMinutes(5L), new Image(imageFile, image -> "progressImage.jpg"), "인증"
        );
        cycleRepository.save(cycle);

        // then
        assertThat(cycleDetailRepository.findAll()).hasSize(1);
    }
}
