package com.woowacourse.smody.cycle.repository;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.support.RepositoryTest;
import com.woowacourse.smody.support.ResourceFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CycleRepositoryTest extends RepositoryTest {

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
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, today.minusDays(3L));

        // when
        List<Cycle> cycles = cycleRepository.findAllByStartTimeIsAfter(today.minusDays(3L));

        // then
        assertAll(
                () -> assertThat(cycles).hasSize(1),
                () -> assertThat(cycles.get(0).getId()).isEqualTo(cycle1.getId())
        );
    }

    @DisplayName("인증이 일어났을 경우 CycleDetail 이 DB 에 저장된다.")
    @Test
    void increaseProgress() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

        // when
        cycle.increaseProgress(now.plusSeconds(60L), 이미지, "인증 완료");

        // then
        List<CycleDetail> cycleDetails = cycleRepository.findById(cycle.getId()).get().getCycleDetailsOrderByProgress();
        assertAll(
                () -> assertThat(cycleDetails).hasSize(1),
                () -> assertThat(cycleDetails.get(0).getProgressImage()).isEqualTo("image.jpg"),
                () -> assertThat(cycleDetails.get(0).getDescription()).isEqualTo("인증 완료")
        );
    }

    @DisplayName("회원의 성공 회수를 포함한 사이클을 기준시간 이후로 조회한다.")
    @Test
    void findByMemberAfterTimeWithSuccessCount() {
        LocalDateTime now = LocalDateTime.now();
        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, now.minusHours(1));
        fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now.minusDays(2L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 스모디_방문하기_ID, now.minusDays(2L));

        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now.minusHours(2));
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(4L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now.minusDays(6L));

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, now.plusSeconds(1L));

        fixture.사이클_생성_SUCCESS(더즈_ID, 오늘의_운동_ID, now.plusSeconds(1L));

        // when
        List<ChallengingRecord> actual = cycleRepository.findAllChallengingRecordByMemberAfterTime(조조그린_ID,
                now.minusDays(3));

        for (ChallengingRecord challengingRecord : actual) {
            String name = challengingRecord.getChallenge().getName();
            System.out.println(name + challengingRecord.getSuccessCount());
        }

        // then
        assertThat(actual).map(ChallengingRecord::getSuccessCount)
                .containsExactly(1, 1, 1, 2, 2, 0);
    }
}
