package com.woowacourse.smody.repository;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ResourceFixture.class)
class CycleRepositoryTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private ResourceFixture fixture;

    @DisplayName("startTime 이 기준시간 이후인 사이클을 조회한다.")
    @Test
    void findAllByStartTimeIsAfter() {
        // given
        LocalDateTime today = LocalDateTime.of(2022, 1, 4, 0, 0);
        Cycle cycle1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, today.minusDays(1L));
        Cycle cycle2 = fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today.minusDays(3L));

        // when
        List<Cycle> cycles = cycleRepository.findAllByStartTimeIsAfter(today.minusDays(3L));

        // then
        assertAll(
                () -> assertThat(cycles.size()).isEqualTo(1),
                () -> assertThat(cycles.get(0).getId()).isEqualTo(cycle1.getId())
        );
    }
}
