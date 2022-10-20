package com.woowacourse.smody.challenge.domain;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class ChallengingRecord {

    private static final int ANY_INDEX = 0;
    private static final int FIRST_INDEX = 0;

    private final List<Cycle> cycles;
    private final Long successCount;

    public ChallengingRecord(List<Cycle> cycles) {
        validateOnlyMember(cycles);
        validateOnlyChallenge(cycles);
        this.cycles = cycles;
        this.successCount = cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
    }

    /**
     * QueryDSL Projection 을 위한 생성자
     */
    public ChallengingRecord(Cycle cycle, Long successCount) {
        this.cycles = List.of(cycle);
        this.successCount = successCount;
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
        return successCount.intValue();
    }

    public LocalDateTime getLatestProgressTime() {
        return cycles.stream()
                .map(Cycle::getLatestProgressTime)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new BusinessException(ExceptionData.DATA_INTEGRITY_ERROR));
    }

    public Challenge getChallenge() {
        return cycles.get(ANY_INDEX)
                .getChallenge();
    }

    public boolean match(Member member) {
        return cycles.get(ANY_INDEX)
                .getMember()
                .equals(member);
    }

    public boolean contains(Cycle cycle) {
        return cycles.stream()
                .anyMatch(c -> c.equals(cycle));
    }

    public boolean isInProgress(LocalDateTime searchTime) {
        return cycles.stream()
                .anyMatch(cycle -> cycle.isInProgress(searchTime));
    }

    public long getDeadLineToMillis(LocalDateTime searchTime) {
        return cycles.stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .findAny()
                .orElseThrow(() -> new BusinessException(ExceptionData.INVALID_PROGRESS_TIME))
                .calculateDeadLineToMillis(searchTime);
    }

    public Cycle getLatestCycle() {
        List<Cycle> sortedCycles = cycles.stream()
                .sorted((cycle1, cycle2) ->
                        (int) ChronoUnit.MILLIS.between(cycle1.getStartTime(), cycle2.getStartTime()))
                .collect(toList());
        return sortedCycles.get(FIRST_INDEX);
    }

    public Integer getCycleDetailCount() {
        return cycles.stream()
                .mapToInt(Cycle::getCycleDetailCount)
                .sum();
    }
}
