package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @DisplayName("모든 챌린지를 조회")
    @Test
    void findAllWithChallengerCount() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member3, challenge1, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member1, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge2, Progress.SUCCESS, today.minusDays(3L)));

        // when
        List<ChallengeResponse> challengeResponses = challengeService.findAllWithChallengerCount(today);

        // then
        assertAll(
                () -> assertThat(challengeResponses.size()).isEqualTo(3),
                () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeName))
                        .containsAll(List.of("스모디 방문하기", "오늘의 운동", "미라클 모닝")),
                () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                        .containsAll(List.of(1, 2, 0))
        );
    }
}
