package com.woowacourse.smody.record.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.record.domain.Record;
import com.woowacourse.smody.record.dto.InProgressResult;
import com.woowacourse.smody.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public void create(Member member, Challenge challenge, LocalDateTime startTime) {
        Optional<Record> myRecord = recordRepository.findByMemberAndAndChallenge(member, challenge);
        if (myRecord.isEmpty()) {
            recordRepository.save(new Record(member, challenge, 0,
                    0, startTime.plusDays(1L), false));
        }
        else {
            Record record = myRecord.get();
            record.changeDeadLine(startTime, 1L);
            record.toNotSuccess();
        }
    }

    @Transactional
    public void increaseDeadLine(Cycle cycle) {
        Member member = cycle.getMember();
        Challenge challenge = cycle.getChallenge();
        LocalDateTime startTime = cycle.getStartTime();
        Record record = findByMemberAndChallenge(member, challenge);
        if (!cycle.isSuccess()) {
            record.changeDeadLine(startTime, (long) (cycle.getProgress().getCount() + 1));
            record.toNotSuccess();
        }
        else {
            record.changeDeadLine(startTime, 3L);
            record.toSuccess();
        }
    }

    private Record findByMemberAndChallenge(Member member, Challenge challenge) {
        return recordRepository.findByMemberAndAndChallenge(member, challenge)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_RECORD));
    }

    public Map<Long, Long> countChallengersIn(List<Challenge> challenges, LocalDateTime startTime) {
        return recordRepository.countChallengersMultipleChallengeNaive(challenges, startTime).stream()
                .collect(Collectors.toMap(each -> each[0], each -> each[1]));
    }

    public Map<Long, Boolean> calculateInProgressIn(Member member, List<Challenge> challenges, LocalDateTime startTime) {
        if (member.getId() == null || member.getId() == 0L) {
            return Collections.emptyMap();
        }
        return recordRepository.isInProgressMultipleChallenge(member, challenges, startTime).stream()
                .collect(Collectors.toMap(each -> each, each -> true));
    }

    public Long countChallengers(Challenge challenge, LocalDateTime startTime) {
        return recordRepository.countChallengersSingleChallenge(challenge, startTime);
    }

    public Map<Long, Boolean> calculateInProgress(Member member, Challenge challenge, LocalDateTime startTime) {
        if (member.getId() == null || member.getId() == 0L) {
            return Collections.emptyMap();
        }
        Long inProgressResult = recordRepository.isInProgressSingleChallenge(member, challenge, startTime);
        return Map.of(inProgressResult, true);
    }
}
