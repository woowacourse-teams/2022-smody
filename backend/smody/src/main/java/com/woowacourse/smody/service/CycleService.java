package com.woowacourse.smody.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleService {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;

    @Transactional
    public Long create(TokenPayload tokenPayload, CycleRequest cycleRequest) {
        Member member = searchMember(tokenPayload);
        Challenge challenge = searchChallenge(cycleRequest);
        if (isDuplicateInProgress(cycleRequest, member, challenge)) {
            throw new BusinessException(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
        }
        Cycle cycle = cycleRepository.save(new Cycle(member, challenge, Progress.NOTHING, cycleRequest.getStartTime()));
        return cycle.getId();
    }

    private boolean isDuplicateInProgress(CycleRequest cycleRequest, Member member, Challenge challenge) {
        return cycleRepository.findAllByMemberAndChallengeAndStartTimeIsAfter(
                        member,
                        challenge,
                        cycleRequest.getStartTime().minusDays(3L)
                ).stream()
                .anyMatch(cycle -> cycle.isInProgress(cycleRequest.getStartTime()));
    }

    public CycleResponse findById(Long cycleId) {
        Cycle cycle = searchCycle(cycleId);
        return new CycleResponse(cycle, calculateSuccessCount(cycle));
    }

    @Transactional
    public ProgressResponse increaseProgress(TokenPayload tokenPayload, ProgressRequest progressRequest) {
        Cycle cycle = searchCycle(progressRequest.getCycleId());
        if (!cycle.matchMember(tokenPayload.getId())) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
        cycle.increaseProgress(progressRequest.getProgressTime());
        return new ProgressResponse(cycle.getProgress());
    }

    public List<CycleResponse> findAllInProgressOfMine(TokenPayload tokenPayload, LocalDateTime searchTime) {
        Member member = searchMember(tokenPayload);
        List<Cycle> inProgressCycles = cycleRepository.findAllByMemberAndStartTimeIsAfter(
                        member, searchTime.minusDays(3L)
                )
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());

        return inProgressCycles.stream()
                .map(cycle -> new CycleResponse(cycle, calculateSuccessCount(cycle)))
                .collect(toList());
    }

    private int calculateSuccessCount(Cycle cycle) {
        return cycleRepository.countByMemberAndChallengeAndProgress(
                cycle.getMember(), cycle.getChallenge(), Progress.SUCCESS
        ).intValue();
    }

    private Member searchMember(TokenPayload tokenPayload) {
        return memberRepository.findById(tokenPayload.getId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }

    private Challenge searchChallenge(CycleRequest cycleRequest) {
        return challengeRepository.findById(cycleRequest.getChallengeId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CHALLENGE));
    }

    private Cycle searchCycle(Long cycleId) {
        return cycleRepository.findById(cycleId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE));
    }
}
