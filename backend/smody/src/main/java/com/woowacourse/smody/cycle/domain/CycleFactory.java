package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class CycleFactory {

    private final CycleRepository cycleRepository;

    public Cycle create(Member member, Challenge challenge, LocalDateTime startTime) {
        Optional<Cycle> optionalCycle = cycleRepository.findRecent(member.getId(), challenge.getId());
        if (optionalCycle.isPresent()) {
            startTime = calculateNewStartTime(startTime, optionalCycle.get());
        }
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING, startTime);
        cycleRepository.save(cycle);
        return cycle;
    }

    private LocalDateTime calculateNewStartTime(LocalDateTime startTime, Cycle cycle) {
        if (cycle.isInProgress(startTime)) {
            throw new BusinessException(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
        }
        if (cycle.isRetry(startTime)) {
            return cycle.getStartTime().plusDays(Cycle.DAYS);
        }
        return startTime;
    }
}
