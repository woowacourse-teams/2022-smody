package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.record.domain.Record;
import com.woowacourse.smody.record.repository.RecordRepository;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.repository.CycleDetailRepository;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.woowacourse.smody.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleService {

    private final CycleRepository cycleRepository;
    private final CycleDetailRepository cycleDetailRepository;
    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final RecordService recordService;

    @Transactional
    public Cycle create(Long memberId, Long challengeId, LocalDateTime startTime) {
        Member member = memberService.search(memberId);
        Challenge challenge = challengeService.search(challengeId);
        Optional<Cycle> optionalCycle = cycleRepository.findRecent(member.getId(), challenge.getId());
        if (optionalCycle.isPresent()) {
            startTime = calculateNewStartTime(startTime, optionalCycle.get());
        }
        recordService.create(member, challenge, startTime);
        return cycleRepository.save(new Cycle(member, challenge, Progress.NOTHING, startTime));
    }

    private LocalDateTime calculateNewStartTime(LocalDateTime startTime, Cycle cycle) {
        if (cycle.isInProgress(startTime)) {
            throw new BusinessException(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
        }
        if (isRetry(startTime, cycle)) {
            return cycle.getStartTime().plusDays(Cycle.DAYS);
        }
        return startTime;
    }

    private boolean isRetry(final LocalDateTime startTime, final Cycle cycle) {
        return cycle.isSuccess() && cycle.isInDays(startTime);
    }

    public int countSuccess(Member member, Challenge challenge) {
        return cycleRepository.countSuccess(member, challenge)
                .intValue();
    }

    public Cycle searchCycle(Long cycleId) {
        return cycleRepository.findById(cycleId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE));
    }

    public CycleDetail searchCycleDetail(Long cycleDetailId) {
        return cycleDetailRepository.findById(cycleDetailId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE_DETAIL));
    }

    public Cycle searchWithLock(Long cycleId) {
        return cycleRepository.findByIdWithLock(cycleId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE));
    }

    public Optional<Cycle> findById(Long id) {
        return cycleRepository.findById(id);
    }

    public List<Cycle> findByMember(Member member) {
        return cycleRepository.findByMember(member);
    }

    public List<Cycle> findAllByChallengeAndMember(Long challengeId, Long memberId) {
        return cycleRepository.findAllByChallengeAndMember(challengeId, memberId);
    }

    public List<Cycle> findAllByMemberAndFilter(Member member, PagingParams pagingParams) {
        return cycleRepository.findAllByMemberAndFilter(member.getId(), pagingParams);
    }

    public List<Cycle> findInProgressByChallenges(LocalDateTime searchTime, List<Challenge> challenges) {
        return cycleRepository.findAllByStartTimeIsAfterAndChallengeIn(searchTime.minusDays(Cycle.DAYS), challenges)
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    public List<Cycle> findInProgressByChallenge(LocalDateTime searchTime, Challenge challenge) {
        return cycleRepository.findAllByStartTimeIsAfterAndChallenge(searchTime.minusDays(Cycle.DAYS), challenge)
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    public List<Cycle> findAllByMemberAndChallengeAndFilter(Long memberId,
                                                            Long challengeId,
                                                            PagingParams pagingParams) {
        return cycleRepository.findAllByMemberAndChallengeAndFilter(memberId, challengeId, pagingParams);
    }

    public List<ChallengingRecord> findAllChallengingRecordByMember(Member member) {
        return cycleRepository.findAllChallengingRecordByMemberAfterTime(
                member.getId(),
                LocalDateTime.now().minusDays(Cycle.DAYS)
        );
    }

    public List<Cycle> findInProgress(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }
}
