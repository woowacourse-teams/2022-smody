package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.common.PagingParams;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.dto.ProgressResponse;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.event.PushEvent;
import java.time.LocalDateTime;
import java.util.Comparator;
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

    public List<Cycle> searchInProgressByMember(LocalDateTime searchTime, Long endTime, Member member, PagingParams pagingParams) {
        List<Cycle> cycles = cycleRepository.findByMemberAfterTime(member, searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .sorted(Comparator.comparing(cycle -> cycle.calculateEndTime(searchTime)))
                .collect(toList());
        Optional<Cycle> lastCycle = cycleRepository.findById(pagingParams.getDefaultCursorId());
        if (lastCycle.isEmpty()) {
            return cycles.subList(0, Math.min(cycles.size(), pagingParams.getDefaultSize()));
        }
        int idx = cycles.indexOf(lastCycle.get());
        return cycles.subList(idx + 1, Math.min(cycles.size(), idx + 1 + pagingParams.getDefaultSize()));
    }

    public List<Cycle> searchInProgress(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    public List<Cycle> searchByMemberAndChallengeWithFilter(Long memberId, Long challengeId, LocalDateTime lastTime, PagingParams pagingParams) {
        return cycleRepository.findAllFilterBy(
                memberId, challengeId, lastTime, pagingParams);
    }

    public Optional<Cycle> findById(Long id) {
        return cycleRepository.findById(id);
    }

    public List<Cycle> searchByMember(Member member) {
        return cycleRepository.findByMember(member);
    }

    public List<Cycle> findAllByChallengeIdAndMemberId(Long challengeId, Long memberId) {
        return cycleRepository.findAllByChallengeIdAndMemberId(challengeId, memberId);
    }

    public List<Cycle> findByMemberWithFilter(Member member, PagingParams pagingParams) {
        return cycleRepository.findByMemberWithFilter(member.getId(), pagingParams);
    }
}
