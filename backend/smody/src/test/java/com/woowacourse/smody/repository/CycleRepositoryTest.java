package com.woowacourse.smody.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;

import com.woowacourse.smody.dto.TokenPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class CycleRepositoryTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("startTime 이 기준시간 이후인 사이클을 조회한다.")
    @Test
    void findAllByStartTimeIsAfter() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Challenge challenge1 = new Challenge("공부");
        Challenge challenge2 = new Challenge("운동");
        memberRepository.save(member1);
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);

        LocalDateTime today = LocalDateTime.of(2022, 1, 4, 0, 0);

        Cycle cycle1 = new Cycle(member1, challenge1, Progress.FIRST, today.minusDays(1L));
        Cycle cycle2 = new Cycle(member1, challenge2, Progress.SUCCESS, today.minusDays(3L));
        cycleRepository.save(cycle1);
        cycleRepository.save(cycle2);

        // when
        List<Cycle> cycles = cycleRepository.findAllByStartTimeIsAfter(today.minusDays(3L));

        // then
        assertAll(
                () -> assertThat(cycles.size()).isEqualTo(1),
                () -> assertThat(cycles.get(0).getId()).isEqualTo(cycle1.getId())
       );
    }
}
