package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleDetailResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        LocalDateTime lastTime = generateBaseStartTime(pagingParams.getDefaultCursorId());
        List<Cycle> cycles = cycleService.searchByMemberAndChallengeWithFilter(
                tokenPayload.getId(), challengeId, lastTime, pagingParams
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

    private LocalDateTime generateBaseStartTime(Long cycleId) {
        Optional<Cycle> lastCycle = cycleService.findById(cycleId);
        if (lastCycle.isEmpty()) {
            return LocalDateTime.now();
        }
        Cycle cycle = lastCycle.get();
        return cycle.getStartTime();
    }
}
