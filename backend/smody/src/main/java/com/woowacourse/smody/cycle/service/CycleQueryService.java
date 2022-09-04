package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.CursorPaging;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleService cycleService;
    private final CycleRepository cycleRepository;
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
        return searchInProgressByMember(searchTime, member, pagingParams).stream()
                .map(cycle -> new InProgressCycleResponse(cycle, cycleService.countSuccess(cycle)))
                .collect(toList());
    }

    private List<Cycle> searchInProgressByMember(LocalDateTime searchTime, Member member, PagingParams pagingParams) {
        List<Cycle> sortedInProgressCycles = cycleService.searchInProgressByMember(searchTime, member).stream()
                .sorted(Comparator.comparing(cycle -> cycle.calculateEndTime(searchTime)))
                .collect(toList());
        Integer size = pagingParams.getDefaultSize();
        return cycleService.findById(pagingParams.getDefaultCursorId())
                .filter(sortedInProgressCycles::contains)
                .map(cursor -> CursorPaging.apply(sortedInProgressCycles, cursor, size))
                .orElse(CursorPaging.apply(sortedInProgressCycles, null, size));
    }

    public StatResponse searchStat(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.searchByMember(member);
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        return new StatResponse(cycles.size(), successCount);
    }

    public List<FilteredCycleHistoryResponse> findAllByMemberAndChallenge(TokenPayload tokenPayload,
                                                                          Long challengeId,
                                                                          PagingParams pagingParams) {
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> cycles = cycleRepository.findAllByMemberAndChallenge(tokenPayload.getId(), challengeId, pagingParams);
        return cycles.stream()
                .map(cycle -> new FilteredCycleHistoryResponse(cycle, challenge))
                .collect(toList());
    }
}
