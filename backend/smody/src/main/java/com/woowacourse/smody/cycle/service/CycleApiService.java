package com.woowacourse.smody.cycle.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.ProgressRequest;
import com.woowacourse.smody.cycle.dto.ProgressResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.CursorPaging;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CycleApiService {

    private final CycleService cycleService;
    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final ImageStrategy imageStrategy;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long create(TokenPayload tokenPayload, CycleRequest cycleRequest) {
        Cycle cycle = cycleService.create(
                tokenPayload.getId(), cycleRequest.getChallengeId(), cycleRequest.getStartTime()
        );
        applicationEventPublisher.publishEvent(new CycleCreateEvent(cycle));
        return cycle.getId();
    }

    @Transactional
    public ProgressResponse increaseProgress(TokenPayload tokenPayload, ProgressRequest progressRequest) {
        Cycle cycle = cycleService.searchWithLock(progressRequest.getCycleId());
        validateAuthorizedMember(tokenPayload, cycle);
        Image progressImage = new Image(null, imageStrategy);
        cycle.increaseProgress(progressRequest.getProgressTime(), progressImage, "asd");
        applicationEventPublisher.publishEvent(new CycleProgressEvent(cycle));
        return new ProgressResponse(cycle.getProgress());
    }

    private void validateAuthorizedMember(TokenPayload tokenPayload, Cycle cycle) {
        if (!cycle.matchMember(tokenPayload.getId())) {
            throw new BusinessException(ExceptionData.UNAUTHORIZED_MEMBER);
        }
    }

    public CycleResponse findWithSuccessCountById(Long cycleId) {
        Cycle cycle = cycleService.search(cycleId);
        return new CycleResponse(cycle, cycleService.countSuccess(cycle));
    }

    public List<InProgressCycleResponse> findInProgressByMe(TokenPayload tokenPayload,
                                                            LocalDateTime searchTime,
                                                            PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload.getId());
        Integer size = pagingParams.getSize();
        List<ChallengingRecord> challengingRecords = cursorPaging(pagingParams, size,
                sortByLatest(cycleService.findAllChallengingRecordByMember(member), searchTime));

        return challengingRecords.stream()
                .map(challengingRecord -> new InProgressCycleResponse(
                        challengingRecord.getLatestCycle(), challengingRecord.getSuccessCount()
                ))
                .collect(toList());
    }

    private List<ChallengingRecord> sortByLatest(List<ChallengingRecord> challengingRecords,
                                                 LocalDateTime searchTime) {
        return challengingRecords.stream()
                .filter(challengingRecord -> challengingRecord.isInProgress(searchTime))
                .sorted(Comparator.comparing(challengingRecord -> challengingRecord.getDeadLineToMillis(searchTime)))
                .collect(toList());
    }

    private List<ChallengingRecord> cursorPaging(PagingParams pagingParams, Integer size,
                                                 List<ChallengingRecord> challengingRecords) {
        Cycle cycle = cycleService.findById(pagingParams.getCursorId())
                .orElse(null);
        return challengingRecords.stream()
                .filter(challengingRecord -> challengingRecord.contains(cycle))
                .findAny()
                .map(cursor -> CursorPaging.apply(challengingRecords, cursor, size))
                .orElse(CursorPaging.apply(challengingRecords, null, size));
    }

    public StatResponse searchStat(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload.getId());
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
        List<Cycle> cycles = cycleService.findAllByMemberAndChallengeAndFilter(
                tokenPayload.getId(), challenge.getId(), pagingParams
        );
        return cycles.stream()
                .map(cycle -> new FilteredCycleHistoryResponse(cycle, challenge))
                .collect(toList());
    }
}
