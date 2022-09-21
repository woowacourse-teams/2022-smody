package com.woowacourse.smody.challenge.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChallengingRecord {

    private final List<Cycle> cycles; // 최근 Cycle(member, challenge, 인증 시간), 성공 회수
    private final Long successCount;

    ChallengingRecord(List<Cycle> cycles) {
        validateOnlyMember(cycles);
        validateOnlyChallenge(cycles);
        this.cycles = cycles;
        this.successCount = cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
    }

    public ChallengingRecord(Cycle cycle, Long successCount) {
        this.cycles = List.of(cycle);
        this.successCount = successCount;
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
        return successCount.intValue();
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

    public boolean match(Cycle cycle) {
        return cycles.get(0)
                .equals(cycle);
    }

    public boolean isInProgress(LocalDateTime searchTime) {
        return cycles.get(0)
                .isInProgress(searchTime);
    }

    public long getEndTime(LocalDateTime searchTime) {
        // 진행중인 사이클이 있는지
        return cycles.get(0)
                .calculateEndTime(searchTime);
    }

    public Cycle getCycle() {
        return cycles.get(0);
    }
}
