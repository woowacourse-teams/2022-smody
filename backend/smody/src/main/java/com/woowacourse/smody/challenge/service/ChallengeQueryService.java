package com.woowacourse.smody.challenge.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.challenge.dto.ChallengersResponse;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.db_support.CursorPaging;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeQueryService {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;

    public List<ChallengeTabResponse> findAllWithChallengerCount(LocalDateTime searchTime, PagingParams pagingParams) {
        return findAllWithChallengerCount(new TokenPayload(0L), searchTime, pagingParams);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                                 PagingParams pagingParams) {
        Member member = memberService.findMember(tokenPayload);
        List<Challenge> challenges = challengeService.searchAll(pagingParams);
        List<Cycle> cycles = cycleService.searchInProgressByChallenges(searchTime, challenges);
        ChallengingRecords challengingRecords = ChallengingRecords.from(cycles);
        return getChallengeTabResponses(challenges, member, challengingRecords);
    }

    private List<ChallengeTabResponse> getChallengeTabResponses(List<Challenge> challenges,
                                                                Member member,
                                                                ChallengingRecords challengingRecords) {
        return challenges.stream()
                .map(challenge -> new ChallengeTabResponse(
                        challenge,
                        challengingRecords.countChallenger(challenge),
                        challengingRecords.isChallenging(challenge, member)
                )).collect(toList());
    }

    public ChallengeResponse findWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        return findWithChallengerCount(new TokenPayload(0L), searchTime, challengeId);
    }

    public ChallengeResponse findWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                     Long challengeId) {
        Member member = memberService.findMember(tokenPayload);
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> cycles = cycleService.searchInProgressByChallenge(searchTime, challenge);
        ChallengingRecords challengingRecords = ChallengingRecords.from(cycles);
        return new ChallengeResponse(
                challenge,
                challengingRecords.countChallenger(challenge),
                challengingRecords.isChallenging(challenge, member)
        );
    }

    public List<ChallengeOfMineResponse> searchOfMine(TokenPayload tokenPayload,
                                                      PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload);
        ChallengingRecords challengingRecords = ChallengingRecords.from(
                cycleService.findAllByMember(member, pagingParams)
        );
        List<ChallengingRecord> sortedRecords = challengingRecords.sortByLatestProgressTime();

        ChallengingRecord cursorChallengingRecord = getCursorMemberChallenge(pagingParams, sortedRecords);
        List<ChallengingRecord> pagedMyChallengeHistories = CursorPaging.apply(
                sortedRecords, cursorChallengingRecord, pagingParams.getDefaultSize()
        );

        return pagedMyChallengeHistories.stream()
                .map(ChallengeOfMineResponse::new)
                .collect(toList());
    }

    private ChallengingRecord getCursorMemberChallenge(PagingParams pagingParams,
                                                       List<ChallengingRecord> myChallengeHistories) {
        return challengeService.findById(pagingParams.getDefaultCursorId())
                .map(cursor -> extractMatchChallenge(myChallengeHistories, cursor))
                .orElse(null);
    }

    private ChallengingRecord extractMatchChallenge(List<ChallengingRecord> myChallengeHistories,
                                                    Challenge lastChallenge) {
        return myChallengeHistories.stream()
                .filter(memberChallenge -> memberChallenge.getChallenge().equals(lastChallenge))
                .findAny()
                .orElse(null);
    }

    public List<ChallengersResponse> findAllChallengers(Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> cycles = cycleService.searchInProgressByChallenge(LocalDateTime.now(), challenge);
        return cycles.stream()
                .map(cycle -> new ChallengersResponse(
                        cycle.getMember(), cycle.getProgress().getCount()))
                .collect(toList());
    }

    public ChallengeHistoryResponse findWithMine(TokenPayload tokenPayload, Long challengeId) {
        List<Cycle> cycles = cycleService.findAllByChallengeIdAndMemberId(challengeId, tokenPayload.getId()).stream()
                .filter(cycle -> !cycle.isInProgress(LocalDateTime.now()))
                .collect(toList());
        Challenge challenge = challengeService.search(challengeId);
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        int cycleDetailCount = cycles.stream()
                .mapToInt(cycle -> cycle.getCycleDetails().size())
                .sum();
        return new ChallengeHistoryResponse(challenge, successCount, cycleDetailCount);
    }
}
