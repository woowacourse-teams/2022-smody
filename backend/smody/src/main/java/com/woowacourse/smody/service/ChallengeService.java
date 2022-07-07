package com.woowacourse.smody.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeService {

    private static final long CYCLE_DAYS = 3L;

    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;

    public List<ChallengeResponse> findAllWithChallengerCount(LocalDateTime searchTime) {
        List<Cycle> inProgressCycles = cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(CYCLE_DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
        return challengeRepository.findAll()
                .stream()
                .map(challenge -> new ChallengeResponse(
                        challenge.getId(),
                        challenge.getName(),
                        (int) inProgressCycles.stream()
                                .filter(cycle -> cycle.matchChallenge(challenge.getId()))
                                .count()
                )).collect(toList());
    }
}
