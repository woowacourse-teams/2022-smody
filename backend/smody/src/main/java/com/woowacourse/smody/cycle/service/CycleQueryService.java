package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.CursorPaging;
import com.woowacourse.smody.db_support.PagingParams;
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
        Integer size = pagingParams.getDefaultSize();
        List<ChallengingRecord> challengingRecords = cursorPaging(pagingParams, size,
                sortByLatest(findAllChallengingRecordByMember(member), searchTime));

        return challengingRecords.stream()
                .map(challengingRecord ->new InProgressCycleResponse(
                        challengingRecord.getCycle(), challengingRecord.getSuccessCount()))
                .collect(toList());
    }

    private List<ChallengingRecord> findAllChallengingRecordByMember(Member member) {
        return cycleRepository.findAllChallengingRecordByMemberAfterTime(
                member.getId(),
                LocalDateTime.now().minusDays(Cycle.DAYS)
        );
    }

    private List<ChallengingRecord> sortByLatest(List<ChallengingRecord> challengingRecords,
                                                   LocalDateTime searchTime) {
        return challengingRecords.stream()
                .filter(challengingRecord -> challengingRecord.isInProgress(searchTime))
                .sorted(Comparator.comparing(challengingRecord -> challengingRecord.getEndTime(searchTime)))
                .collect(toList());
    }

    private List<ChallengingRecord> cursorPaging(PagingParams pagingParams, Integer size,
                                                 List<ChallengingRecord> challengingRecords) {
        Cycle cycle = cycleService.findById(pagingParams.getDefaultCursorId())
                .orElse(null);
        return challengingRecords.stream()
                .filter(challengingRecord -> challengingRecord.match(cycle))
                .findAny()
                .map(cursor -> CursorPaging.apply(challengingRecords, cursor, size))
                .orElse(CursorPaging.apply(challengingRecords, null, size));
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
        List<Cycle> cycles = cycleRepository.findAllByMemberAndChallenge(tokenPayload.getId(), challengeId,
                pagingParams);
        return cycles.stream()
                .map(cycle -> new FilteredCycleHistoryResponse(cycle, challenge))
                .collect(toList());
    }
}
