package com.woowacourse.smody.record.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.record.domain.Record;
import com.woowacourse.smody.record.dto.ChallengersResult;
import com.woowacourse.smody.record.dto.InProgressResult;
import com.woowacourse.smody.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
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

    public Map<Long, Long> countChallengers(List<Challenge> challenges, LocalDateTime startTime) {
        return recordRepository.countChallengers(challenges, startTime).stream()
                .collect(Collectors.toMap(ChallengersResult::getChallengeId, ChallengersResult::getChallengers));
    }

    public Map<Long, Boolean> calculateInProgress(Member member, List<Challenge> challenges, LocalDateTime startTime) {
        if (member.getId() == null || member.getId() == 0L) {
            return Collections.emptyMap();
        }
        return recordRepository.isInProgress(member, challenges, startTime).stream()
                .collect(Collectors.toMap(InProgressResult::getChallengeId, InProgressResult::isInProgress));
    }
}
