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
            Cycle cycle = optionalCycle.get();
            startTime = cycle.calculateRetryStartTime(startTime);
        }
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING, startTime);
        cycleRepository.save(cycle);
        return cycle;
    }
}
