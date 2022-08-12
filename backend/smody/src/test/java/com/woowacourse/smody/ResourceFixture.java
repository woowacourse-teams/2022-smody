package com.woowacourse.smody;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("NonAsciiCharacters")
public class ResourceFixture {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;

    public ResourceFixture(final MemberRepository memberRepository,
                           final ChallengeRepository challengeRepository,
                           final CycleRepository cycleRepository) {
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        this.cycleRepository = cycleRepository;
    }

    public static final Long 조조그린_ID = 1L;
    public static final Long 더즈_ID = 2L;
    public static final Long 토닉_ID = 3L;
    public static final Long 알파_ID = 4L;
    public static final Long 스모디_방문하기_ID = 1L;
    public static final Long 미라클_모닝_ID = 2L;
    public static final Long 오늘의_운동_ID = 3L;
    public static final Long 알고리즘_풀기_ID = 4L;
    public static final Long JPA_공부_ID = 5L;

    public Member 회원_조회(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Challenge 챌린지_조회(Long id) {
        return challengeRepository.findById(id).orElseThrow();
    }

    public Cycle 사이클_생성(Long memberId, Long challengeId, Progress progress, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), progress, startTime);
        return cycleRepository.save(cycle);
    }

    public Cycle 사이클_생성_NOTHING(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return cycleRepository.save(cycle);
    }

    public Cycle 사이클_생성_FIRST(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.FIRST, startTime);
        return cycleRepository.save(cycle);
    }

    public Cycle 사이클_생성_SECOND(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.SECOND, startTime);
        return cycleRepository.save(cycle);
    }

    public Cycle 사이클_생성_SUCCESS(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.SUCCESS, startTime);
        return cycleRepository.save(cycle);
    }
}
