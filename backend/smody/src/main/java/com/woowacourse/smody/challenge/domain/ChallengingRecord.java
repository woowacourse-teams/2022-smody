package com.woowacourse.smody.challenge.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ChallengingRecord {

    private final List<Cycle> cycles;

    ChallengingRecord(List<Cycle> cycles) {
        validateOnlyMember(cycles);
        validateOnlyChallenge(cycles);
        this.cycles = cycles;
    }

    public static List<ChallengingRecord> from(List<Cycle> cycles) {
        return cycles.stream()
                .filter(cycle -> cycle.isInProgress(LocalDateTime.now()))
                .collect(groupingBy(Cycle::getChallenge, toList()))
                .values()
                .stream()
                .map(ChallengingRecord::new)
                .collect(toList());
    }

    private void validateOnlyMember(List<Cycle> cycles) {
        if (cycles.stream()
                .map(Cycle::getMember)
                .distinct()
                .count() != 1L) {
            throw new BusinessException(ExceptionData.DATA_INTEGRITY_ERROR);
        }
    }

    private void validateOnlyChallenge(List<Cycle> cycles) {
        if (cycles.stream()
                .map(Cycle::getChallenge)
                .distinct()
                .count() != 1L) {
            throw new BusinessException(ExceptionData.DATA_INTEGRITY_ERROR);
        }
    }

    public int getSuccessCount() {
        return (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
    }

    public LocalDateTime getLatestProgressTime() {
        return cycles.stream()
                .map(Cycle::getLatestProgressTime)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new BusinessException(ExceptionData.DATA_INTEGRITY_ERROR));
    }

    public Challenge getChallenge() {
        return cycles.get(0)
                .getChallenge();
    }

    public boolean match(Member member) {
        return cycles.get(0)
                .getMember()
                .equals(member);
    }
}
