package com.woowacourse.smody.challenge.domain;

import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChallengingRecordTest {

    @DisplayName("MemberChallenge는 같은 챌린지, 같은 회원을 가지고 있어야 한다.")
    @Test
    void newMemberChallenge() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Challenge challenge = new Challenge("미라클 모닝");
        Member member = new Member("does@mail.com", "does", "picture");
        List<Cycle> cycles = List.of(
                new Cycle(member, challenge, Progress.NOTHING, now),
                new Cycle(member, challenge, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member, challenge, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(6L))
        );

        // when then
        assertDoesNotThrow(() -> new ChallengingRecord(cycles));
    }

    @DisplayName("MemberChallenge는 같은 회원을 가지고 있어야 한다.")
    @Test
    void newMemberChallenge_member_exception() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Challenge challenge = new Challenge("미라클 모닝");
        Member member1 = new Member("does@mail.com", "does", "picture");
        Member member2 = new Member("tonic@mail.com", "tonic", "picture");
        List<Cycle> cycles = List.of(
                new Cycle(member1, challenge, Progress.NOTHING, now),
                new Cycle(member1, challenge, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member1, challenge, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member2, challenge, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member1, challenge, Progress.SUCCESS, now.minusDays(6L))
        );

        // when then
        assertThatThrownBy(() -> new ChallengingRecord(cycles))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DATA_INTEGRITY_ERROR);
    }

    @DisplayName("MemberChallenge는 같은 챌린지를 가지고 있어야 한다.")
    @Test
    void newMemberChallenge_challenge_exception() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Challenge challenge1 = new Challenge("미라클 모닝");
        Challenge challenge2 = new Challenge("운동하기");
        Member member1 = new Member("does@mail.com", "does", "picture");
        List<Cycle> cycles = List.of(
                new Cycle(member1, challenge1, Progress.NOTHING, now),
                new Cycle(member1, challenge1, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member1, challenge1, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member1, challenge2, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member1, challenge1, Progress.SUCCESS, now.minusDays(6L))
        );

        // when then
        assertThatThrownBy(() -> new ChallengingRecord(cycles))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DATA_INTEGRITY_ERROR);
    }

    @DisplayName("MemberChallenge에 사이클이 없으면 예외를 발생시킨다.")
    @Test
    void newMemberChallenge_empty_exception() {
        // given
        List<Cycle> cycles = new ArrayList<>();

        // when then
        assertThatThrownBy(() -> new ChallengingRecord(cycles))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DATA_INTEGRITY_ERROR);
    }

    @DisplayName("성공한 사이클 횟수를 반환한다.")
    @Test
    void getSuccessCount() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Challenge challenge = new Challenge("미라클 모닝");
        Member member = new Member("does@mail.com", "does", "picture");
        List<Cycle> cycles = List.of(
                new Cycle(member, challenge, Progress.NOTHING, now),
                new Cycle(member, challenge, Progress.FIRST, now.minusDays(1L)),
                new Cycle(member, challenge, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(6L))
        );
        ChallengingRecord challengingRecord = new ChallengingRecord(cycles);

        // when
        int actual = challengingRecord.getSuccessCount();

        // then
        assertThat(actual).isEqualTo(2);
    }

    @DisplayName("가장 최근에 인증한 시간을 반환한다.")
    @Test
    void getLatestProgressTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Challenge challenge = new Challenge("미라클 모닝");
        Member member = new Member("does@mail.com", "does", "picture");
        Cycle cycle1 = new Cycle(member, challenge, Progress.NOTHING, now);
        Cycle cycle2 = new Cycle(member, challenge, Progress.FIRST, now.minusDays(1L));
        cycle1.increaseProgress(now.plusMinutes(1), 이미지, "오늘 인증");
        cycle2.increaseProgress(now.plusMinutes(5), 이미지, "어제 인증");
        List<Cycle> cycles = List.of(
                cycle1,
                cycle2,
                new Cycle(member, challenge, Progress.SECOND, now.minusDays(9L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(3L)),
                new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(6L))
        );
        ChallengingRecord challengingRecord = new ChallengingRecord(cycles);

        // when
        LocalDateTime actual = challengingRecord.getLatestProgressTime();

        // then
        assertThat(actual).isEqualTo(now.plusMinutes(5));
    }
}
