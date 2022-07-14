package com.woowacourse.smody.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
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

    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;

    public List<ChallengeResponse> findAllWithChallengerCount(LocalDateTime searchTime) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        return challengeRepository.findAll()
                .stream()
                .map(challenge -> new ChallengeResponse(
                        challenge,
                        calculateCountByChallenge(inProgressCycles, challenge)
                ))
                .collect(toList());
    }

    private List<Cycle> searchInProgressCycles(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    private int calculateCountByChallenge(List<Cycle> cycles, Challenge challenge) {
        return (int) cycles.stream()
                .filter(cycle -> cycle.matchChallenge(challenge.getId()))
                .count();
    }
}
