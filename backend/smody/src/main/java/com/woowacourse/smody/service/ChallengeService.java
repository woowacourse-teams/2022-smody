package com.woowacourse.smody.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.repository.CycleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeService {

    private static final long CYCLE_DAYS = 3L;

    private final CycleRepository cycleRepository;

    public List<ChallengeResponse> findAllWithChallengerCount(LocalDateTime searchTime) {
        List<Cycle> inProgressCycles = cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(CYCLE_DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
        Map<Challenge, Long> result = inProgressCycles.stream()
                .collect(groupingBy(Cycle::getChallenge, counting()));
        return result.entrySet().stream()
                .map(entry -> new ChallengeResponse(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getValue().intValue()
                )).collect(toList());
    }
}
