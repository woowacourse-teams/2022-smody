package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.dto.FeedResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
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
    private ResourceFixture resourceFixture;

    @Autowired
    private EntityManager em;

    @DisplayName("모든 피드 조회 시 id 순으로 조회한다")
    @Test
    void searchAllSortedTime() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, today);
        makeSuccessCycle(cycle, today);

        // when
        List<FeedResponse> feedResponses = feedQueryService.searchAll(10, 0L);

        //then
        assertAll(
                () -> assertThat(feedResponses).hasSize(3),
                () -> assertThat(feedResponses.stream()
                        .map(FeedResponse::getDescription)
                        .collect(Collectors.toList())).containsExactly("인증1", "인증2", "인증3")
        );
    }

    @DisplayName("모든 피드 조회 시 10개 씩 조회한다.")
    @Test
    void searchAllSize10() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle1 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, today);
        Cycle cycle2 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, today);
        Cycle cycle3 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, today);
        Cycle cycle4 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 알고리즘_풀기_ID, today);
        makeSuccessCycle(cycle1, today);
        makeSuccessCycle(cycle2, today);
        makeSuccessCycle(cycle3, today);
        makeSuccessCycle(cycle4, today);

        em.flush();
        // when
        List<FeedResponse> feedResponses = feedQueryService.searchAll(10, cycle1.getCycleDetails().get(0).getId());

        // then
        assertThat(feedResponses).hasSize(10);
    }

    @DisplayName("모든 피드 조회 시 지정된 Id 이 후에 생성된 데이터를 조회한다.")
    @Test
    void searchByCycleDetailId() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Cycle cycle1 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, today);
        Cycle cycle2 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, today);
        Cycle cycle3 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, today);
        Cycle cycle4 = resourceFixture.사이클_생성_NOTHING(조조그린_ID, 알고리즘_풀기_ID, today);
        makeSuccessCycle(cycle1, today);
        makeSuccessCycle(cycle2, today);
        makeSuccessCycle(cycle3, today);
        makeSuccessCycle(cycle4, today);
        em.flush();
        // when
        List<FeedResponse> feedResponses = feedQueryService.searchAll(
                10, cycle2.getCycleDetails().get(0).getId()
        );

        // then
        assertAll(
                () -> assertThat(feedResponses).hasSize(8),
                () -> assertThat(feedResponses.stream()
                        .map(FeedResponse::getCycleDetailId)
                        .collect(Collectors.toList())).containsExactly(
                        cycle2.getCycleDetails().get(1).getId(),
                        cycle2.getCycleDetails().get(2).getId(),
                        cycle3.getCycleDetails().get(0).getId(),
                        cycle3.getCycleDetails().get(1).getId(),
                        cycle3.getCycleDetails().get(2).getId(),
                        cycle4.getCycleDetails().get(0).getId(),
                        cycle4.getCycleDetails().get(1).getId(),
                        cycle4.getCycleDetails().get(2).getId()
                )
        );
    }

    private void makeSuccessCycle(Cycle cycle, LocalDateTime today) {
        MultipartFile imageFile = new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        );
        cycle.increaseProgress(today.plusMinutes(1L),
                new Image(imageFile, image -> "image.jpg"), "인증1");
        cycle.increaseProgress(today.plusDays(1L).plusMinutes(1L),
                new Image(imageFile, image -> "image.jpg"), "인증2");
        cycle.increaseProgress(today.plusDays(2L).plusMinutes(1L),
                new Image(imageFile, image -> "image.jpg"), "인증3");
    }
}
