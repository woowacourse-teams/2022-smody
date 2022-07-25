package com.woowacourse.smody.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.woowacourse.smody.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        Optional<Cycle> optionalCycle = cycleRepository.findRecent(member, challenge);

        LocalDateTime startTime = cycleRequest.getStartTime();
        if (optionalCycle.isPresent()) {
            startTime = calculateNewStartTime(startTime, optionalCycle.get());
        }
        Cycle save = cycleRepository.save(new Cycle(member, challenge, Progress.NOTHING, startTime));
        return save.getId();
    }

    private LocalDateTime calculateNewStartTime(LocalDateTime startTime, Cycle cycle) {
        if (cycle.isInProgress(startTime)) {
            throw new BusinessException(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
        }
        if (cycle.isSuccess() && cycle.isInDays(startTime)) {
            return cycle.getStartTime().plusDays(Cycle.DAYS);
        }
        return startTime;
    }

    public CycleResponse findById(Long cycleId) {
        Cycle cycle = searchCycle(cycleId);
        return new CycleResponse(cycle, calculateSuccessCount(cycle));
    }

    @Transactional
    public ProgressResponse increaseProgress(TokenPayload tokenPayload, ProgressRequest progressRequest) {
        Cycle cycle = searchCycle(progressRequest.getCycleId());
        validateAuthorizedMember(tokenPayload, cycle);
        cycle.increaseProgress(progressRequest.getProgressTime());
        return new ProgressResponse(cycle.getProgress());
    }

    private void validateAuthorizedMember(TokenPayload tokenPayload, Cycle cycle) {
        if (!cycle.matchMember(tokenPayload.getId())) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
    }

    public List<CycleResponse> findAllInProgressOfMine(TokenPayload tokenPayload, LocalDateTime searchTime,
        Pageable pageable) {
        Member member = searchMember(tokenPayload);
        List<Cycle> inProgressCycles = searchInProgressCycleByMember(searchTime, member);
        inProgressCycles.sort(Comparator.comparingLong(cycle -> cycle.calculateEndTime(searchTime)));
        List<Cycle> pagedCycles = PagingUtil.page(inProgressCycles, pageable);
        return pagedCycles.stream()
                .map(cycle -> new CycleResponse(cycle, calculateSuccessCount(cycle)))
                .collect(toList());
    }

    private List<Cycle> searchInProgressCycleByMember(LocalDateTime searchTime, Member member) {
        return cycleRepository.findByMemberAfterTime(member, searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    private int calculateSuccessCount(Cycle cycle) {
        return cycleRepository.countSuccess(cycle.getMember(), cycle.getChallenge())
                .intValue();
    }

    public StatResponse searchStat(TokenPayload tokenPayload) {
        Member member = searchMember(tokenPayload);
        List<Cycle> cycles = cycleRepository.findByMember(member);
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        return new StatResponse(cycles.size(), successCount);
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
