package com.woowacourse.smody.service;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.woowacourse.smody.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.ProgressResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.ImageStrategy;
import com.woowacourse.smody.push.event.PushEvent;
import com.woowacourse.smody.repository.CycleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleService {

    private final CycleRepository cycleRepository;
    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ImageStrategy imageStrategy;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long create(TokenPayload tokenPayload, CycleRequest cycleRequest) {
        Member member = memberService.search(tokenPayload);
        Challenge challenge = challengeService.search(cycleRequest.getChallengeId());
        Optional<Cycle> optionalCycle = cycleRepository.findRecent(member, challenge);

        LocalDateTime startTime = cycleRequest.getStartTime();
        if (optionalCycle.isPresent()) {
            startTime = calculateNewStartTime(startTime, optionalCycle.get());
        }
        Cycle save = cycleRepository.save(new Cycle(member, challenge, Progress.NOTHING, startTime));

        applicationEventPublisher.publishEvent(new PushEvent(save, PushCase.CHALLENGE));
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

    @Transactional
    public ProgressResponse increaseProgress(TokenPayload tokenPayload, ProgressRequest progressRequest) {
        Cycle cycle = searchWithLock(progressRequest.getCycleId());
        validateAuthorizedMember(tokenPayload, cycle);
        Image progressImage = new Image(progressRequest.getProgressImage(), imageStrategy);
        cycle.increaseProgress(progressRequest.getProgressTime(), progressImage, progressRequest.getDescription());

        applicationEventPublisher.publishEvent(new PushEvent(cycle, PushCase.CHALLENGE));
        return new ProgressResponse(cycle.getProgress());
    }

    private void validateAuthorizedMember(TokenPayload tokenPayload, Cycle cycle) {
        if (!cycle.matchMember(tokenPayload.getId())) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
    }

    public int countSuccess(Cycle cycle) {
        return cycleRepository.countSuccess(cycle.getMember(), cycle.getChallenge())
                .intValue();
    }

    public Cycle search(Long cycleId) {
        return cycleRepository.findById(cycleId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE));
    }

    public Cycle searchWithLock(Long cycleId) {
        return cycleRepository.findByIdWithLock(cycleId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CYCLE));
    }

    public List<Cycle> searchInProgressByMember(LocalDateTime searchTime, Member member) {
        return cycleRepository.findByMemberAfterTime(member, searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    public List<Cycle> searchInProgress(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    public List<Cycle> searchByMemberAndChallenge(Long memberId, Long challengeId, CycleHistoryRequest cycleHistoryRequest) {
        return cycleRepository.findAllFilterBy(
                memberId, challengeId, cycleHistoryRequest.getFilter(),
                cycleHistoryRequest.getLastCycleDetailId(), cycleHistoryRequest.toPageRequest());
    }

    public List<Cycle> findByMember(Member member) {
        return cycleRepository.findByMember(member);
    }

    public List<Cycle> searchByMember(Member member) {
        return cycleRepository.findByMember(member);
    }

    public List<Cycle> findAllByChallengeIdAndMemberId(Long challengeId, Long memberId) {
        return cycleRepository.findAllByChallengeIdAndMemberId(challengeId, memberId);
    }
}
