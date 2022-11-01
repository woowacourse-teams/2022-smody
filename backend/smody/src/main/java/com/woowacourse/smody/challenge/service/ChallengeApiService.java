package com.woowacourse.smody.challenge.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.challenge.domain.ChallengingRecords;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.woowacourse.smody.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeApiService {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;
    private final RecordService recordService;

    public List<ChallengeTabResponse> findAllWithChallengerCountByFilter(LocalDateTime searchTime,
                                                                         PagingParams pagingParams) {
        return findAllWithChallengerCountByFilter(TokenPayload.NOT_LOGIN_TOKEN_PAYLOAD, searchTime, pagingParams);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCountByFilter(TokenPayload tokenPayload,
                                                                         LocalDateTime searchTime,
                                                                         PagingParams pagingParams) {
        Member member = memberService.searchLoginMember(tokenPayload.getId());
        if (pagingParams.getSort().equals("popular")) {
            return getChallengeTabResponsesWhenPopular(searchTime, pagingParams, member);
        }
        if (pagingParams.getSort().equals("random")) {
            return getChallengeTabResponsesWhenRandom(searchTime, pagingParams, member);
        }
        List<Challenge> challenges = challengeService.findAllByFilter(pagingParams);
        Map<Long, Long> count = recordService.countChallengersIn(challenges, searchTime);
        Map<Long, Boolean> inProgressResults = recordService.calculateInProgressIn(member, challenges, searchTime);
        return getChallengeTabResponses(challenges, count, inProgressResults);
    }

    private List<ChallengeTabResponse> getChallengeTabResponsesWhenPopular(LocalDateTime searchTime,
                                                                           PagingParams pagingParams,
                                                                           Member member) {
        List<Cycle> cycles = cycleService.findInProgress(searchTime);
        ChallengingRecords challengingRecords = ChallengingRecords.from(cycles);
        List<Challenge> challenges = challengingRecords.getChallengesOrderByChallenger();
        List<Challenge> pagedChallenges = CursorPaging.apply(challenges, null, pagingParams.getSize());
        return getChallengeTabResponses(pagedChallenges, member, challengingRecords);
    }

    private List<ChallengeTabResponse> getChallengeTabResponsesWhenRandom(final LocalDateTime searchTime,
                                                                          final PagingParams pagingParams,
                                                                          final Member member) {
        List<Challenge> randomChallenges = challengeService.findRandomChallenges(pagingParams.getSize());
        List<Cycle> cycles = cycleService.findInProgress(searchTime);
        ChallengingRecords challengingRecords = ChallengingRecords.from(cycles);
        List<Challenge> pagedChallenges = CursorPaging.apply(randomChallenges, null, pagingParams.getSize());
        return getChallengeTabResponses(pagedChallenges, member, challengingRecords);
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

    private List<ChallengeTabResponse> getChallengeTabResponses(List<Challenge> challenges,
                                                                Map<Long, Long> challengersCount,
                                                                Map<Long, Boolean> inProgress) {
        Map<Challenge, Long> challengersByChallenge = new HashMap<>();
        Map<Challenge, Boolean> inProgressByChallenge = new HashMap<>();
        for (Challenge challenge : challenges) {
            challengersByChallenge.put(challenge, 0L);
            inProgressByChallenge.put(challenge, false);
        }
        challenges.stream()
                .filter(each -> challengersCount.containsKey(each.getId()))
                .forEach(each -> challengersByChallenge.put(each, challengersCount.get(each.getId())));
        challenges.stream()
                .filter(each -> inProgress.containsKey(each.getId()))
                .forEach(each -> inProgressByChallenge.put(each, inProgress.get(each.getId())));
        return challenges.stream()
                .map(each -> new ChallengeTabResponse(each, challengersByChallenge.get(each).intValue(), inProgressByChallenge.get(each)))
                .collect(Collectors.toList());
    }

    public ChallengeResponse findWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        return findWithChallengerCount(TokenPayload.NOT_LOGIN_TOKEN_PAYLOAD, searchTime, challengeId);
    }

    public ChallengeResponse findWithChallengerCount(
            TokenPayload tokenPayload, LocalDateTime searchTime, Long challengeId
    ) {
        Member member = memberService.searchLoginMember(tokenPayload.getId());
        Challenge challenge = challengeService.search(challengeId);
        Map<Long, Long> count = recordService.countChallengers(challenge, searchTime);
        Map<Long, Boolean> inProgressResults = recordService.calculateInProgress(member, challenge, searchTime);
        return new ChallengeResponse(
                challenge,
                count.get(challengeId).intValue(),
                inProgressResults.get(challengeId)
        );
    }

    public List<ChallengeOfMineResponse> findAllByMeAndFilter(TokenPayload tokenPayload,
                                                              PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload.getId());
        ChallengingRecords challengingRecords = ChallengingRecords.from(
                cycleService.findAllByMemberAndFilter(member, pagingParams)
        );
        List<ChallengingRecord> sortedRecords = challengingRecords.sortByLatestProgressTime();

        ChallengingRecord cursorChallengingRecord = getCursorChallengeRecord(pagingParams, sortedRecords);
        List<ChallengingRecord> pagedMyChallengeHistories = CursorPaging.apply(
                sortedRecords, cursorChallengingRecord, pagingParams.getSize()
        );

        return pagedMyChallengeHistories.stream()
                .map(ChallengeOfMineResponse::new)
                .collect(toList());
    }

    private ChallengingRecord getCursorChallengeRecord(PagingParams pagingParams,
                                                       List<ChallengingRecord> myChallengeHistories) {
        return challengeService.findById(pagingParams.getCursorId())
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
        List<Cycle> cycles = cycleService.findInProgressByChallenge(LocalDateTime.now(), challenge);
        return cycles.stream()
                .map(cycle -> new ChallengersResponse(
                        cycle.getMember(), cycle.getProgress().getCount()))
                .collect(toList());
    }

    public ChallengeHistoryResponse findByMeAndChallenge(TokenPayload tokenPayload, Long challengeId) {
        List<Cycle> cycles = cycleService.findAllByChallengeAndMember(challengeId, tokenPayload.getId());
        ChallengingRecord challengingRecord = new ChallengingRecord(cycles);
        return new ChallengeHistoryResponse(
                challengingRecord.getChallenge(),
                challengingRecord.getSuccessCount(),
                challengingRecord.getCycleDetailCount()
        );
    }

    @Transactional
    public Long create(ChallengeRequest challengeRequest) {
        return challengeService.create(
                challengeRequest.getChallengeName(),
                challengeRequest.getDescription(),
                challengeRequest.getEmojiIndex(),
                challengeRequest.getColorIndex()
        );
    }
}
