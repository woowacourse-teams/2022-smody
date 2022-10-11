package com.woowacourse.smody.challenge.domain;

import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChallengingRecordsTest {

    private ChallengingRecords challengingRecords;
    private final Challenge challenge1 = new Challenge("미라클 모닝", "설명", 1, 1);
    private final Member member1 = new Member("does@mail.com", "does", "picture");
    private final Member member2 = new Member("tonic@mail.com", "tonic", "picture");
    private final LocalDateTime now = LocalDateTime.now();

    // challengeRecords의 역할
    // 각 챌린지 인증 시간을 기준으로 정렬할 수 있다.
    @BeforeEach
    void setUp() {
        Challenge challenge2 = new Challenge("운동하기", "설명", 1, 1);
        Cycle cycle = new Cycle(member1, challenge1, Progress.NOTHING, now);
        cycle.increaseProgress(now.plusMinutes(1L), 이미지, "인증");
        List<Cycle> cycles = List.of(
                cycle,
                new Cycle(member1, challenge1, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member1, challenge1, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member1, challenge1, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member1, challenge1, Progress.SUCCESS, now.minusDays(6L)),
                new Cycle(member2, challenge2, Progress.NOTHING, now),
                new Cycle(member2, challenge2, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member2, challenge2, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member2, challenge2, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member2, challenge2, Progress.SUCCESS, now.minusDays(6L))
        );
        challengingRecords = ChallengingRecords.from(cycles);
    }

    @DisplayName("특정 챌린지의 현재 도전자 수를 구한다.")
    @Test
    void findByChallenge() {
        // when
        int actual = challengingRecords.countChallenger(challenge1);

        // then
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("특정 챌린지에 특정 멤버가 도전중이면 true를 반환한다.")
    @Test
    void isChallenging() {
        // when
        boolean actual = challengingRecords.isChallenging(challenge1, member1);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("특정 챌린지에 특정 멤버가 도전중이 아니면 false를 반환한다.")
    @Test
    void isNotChallenging() {
        // when
        boolean actual = challengingRecords.isChallenging(challenge1, member2);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("가장 최근 인증시간으로 정렬한다.")
    @Test
    void sortByLatestProgressTime() {
        // when
        List<ChallengingRecord> actual = challengingRecords.sortByLatestProgressTime();

        // then
        assertThat(actual)
                .map(ChallengingRecord::getLatestProgressTime)
                .containsExactly(now.plusMinutes(1L), now);
    }

}
