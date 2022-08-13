package com.woowacourse.smody.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.util.PagingUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleService cycleService;
    private final MemberService memberService;

    public CycleResponse findById(Long cycleId) {
        Cycle cycle = cycleService.search(cycleId);
        return new CycleResponse(cycle, cycleService.countSuccess(cycle));
    }

    public List<InProgressCycleResponse> findInProgressOfMine(TokenPayload tokenPayload,
                                                              LocalDateTime searchTime,
                                                              Pageable pageable) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> inProgressCycles = cycleService.searchInProgressByMember(searchTime, member);
        inProgressCycles.sort(Comparator.comparingLong(cycle -> cycle.calculateEndTime(searchTime)));
        List<Cycle> pagedCycles = PagingUtil.page(inProgressCycles, pageable);
        return pagedCycles.stream()
                .map(cycle -> new InProgressCycleResponse(cycle, cycleService.countSuccess(cycle)))
                .collect(toList());
    }

    public StatResponse searchStat(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.searchByMember(member);
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        return new StatResponse(cycles.size(), successCount);
    }

    public List<CycleHistoryResponse> findAllWithChallenge(TokenPayload tokenPayload,
                                                           CycleHistoryRequest cycleHistoryRequest,
                                                           Long challengeId) {
        List<Cycle> cycles = cycleService.searchByMemberAndChallenge(
                tokenPayload.getId(), challengeId, cycleHistoryRequest
        );
        return cycles.stream()
                .map(cycle -> new CycleHistoryResponse(cycle.getId(), cycle.getCycleDetails().stream()
                        .map(cycleDetail -> new CycleDetailResponse(
                                cycleDetail.getProgressTime(), cycleDetail.getProgressImage(), cycleDetail.getDescription()))
                        .collect(toList())))
                .collect(toList());
    }
}
