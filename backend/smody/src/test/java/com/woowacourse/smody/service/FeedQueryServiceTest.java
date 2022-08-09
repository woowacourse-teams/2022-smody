package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.dto.FeedResponse;
import com.woowacourse.smody.repository.CycleRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class FeedQueryServiceTest {

    @Autowired
    private FeedQueryService feedQueryService;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private ResourceFixture resourceFixture;

    @DisplayName("모든 피드를 조회한다")
    @Test
    void searchAll() {
        // given
        LocalDateTime today = LocalDateTime.now();
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        Cycle cycle = new Cycle(resourceFixture.회원_조회(조조그린_ID), resourceFixture.챌린지_조회(미라클_모닝_ID),
                Progress.NOTHING, today);
        cycle.increaseProgress(
                today.plusMinutes(5L), new Image(imageFile, image -> "progressImage.jpg"), "인증"
        );
        cycleRepository.save(cycle);

        // when
        List<FeedResponse> feedResponses = feedQueryService.searchAll();

        //then
        assertThat(feedResponses).hasSize(1);
    }
}
