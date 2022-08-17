package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PagingParams;
import com.woowacourse.smody.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleService cycleService;
    private final MemberService memberService;

    private final ChallengeService challengeService;

    public CycleResponse findById(Long cycleId) {
        Cycle cycle = cycleService.search(cycleId);
        return new CycleResponse(cycle, cycleService.countSuccess(cycle));
    }

    public List<InProgressCycleResponse> findInProgressOfMine(TokenPayload tokenPayload,
                                                              LocalDateTime searchTime,
                                                              PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload);
        Long latestEndTime = generateBaseEndTime(pagingParams.getDefaultCursorId());
        List<Cycle> inProgressCycles = cycleService.searchInProgressByMember(searchTime, latestEndTime, member, pagingParams);
        inProgressCycles.sort(Comparator.comparingLong(cycle -> cycle.calculateEndTime(searchTime)));
        List<Cycle> pagedCycles = inProgressCycles.subList(0, Math.min(inProgressCycles.size(), pagingParams.getDefaultSize()));
        return pagedCycles.stream()
                .map(cycle -> new InProgressCycleResponse(cycle, cycleService.countSuccess(cycle)))
                .collect(toList());
    }

    private Long generateBaseEndTime(Long cycleId) {
        Optional<Cycle> lastCycle = cycleService.findById(cycleId);
        if (lastCycle.isEmpty()) {
            return Long.MIN_VALUE;
        }
        Cycle cycle = lastCycle.get();
        return cycle.calculateEndTime(LocalDateTime.now());
    }

    public StatResponse searchStat(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.searchByMember(member);
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        return new StatResponse(cycles.size(), successCount);
    }

    public List<FilteredCycleHistoryResponse> findAllByMemberAndChallengeWithFilter(TokenPayload tokenPayload,
                                                                                    Long challengeId,
                                                                                    PagingParams pagingParams) {
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> cycles = cycleService.searchByMemberAndChallengeWithFilter(
                tokenPayload.getId(), challengeId, pagingParams
        );
        return cycles.stream()
                .map(cycle -> new FilteredCycleHistoryResponse(
                        cycle.getId(), challenge.getEmojiIndex(), challenge.getColorIndex(), cycle.getStartTime(),
                        cycle.getCycleDetails().stream()
                                .map(
                                        cycleDetail -> new FilteredCycleDetailResponse(
                                                cycleDetail.getId(), cycleDetail.getProgressImage()
                                        )
                                )
                        .collect(toList())))
                .collect(toList());
    }
}
