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
import java.util.List;
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

    public List<ChallengeTabResponse> findAllWithChallengerCountByFilter(LocalDateTime searchTime,
                                                                         PagingParams pagingParams) {
        return findAllWithChallengerCountByFilter(new TokenPayload(0L), searchTime, pagingParams);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCountByFilter(
            TokenPayload tokenPayload, LocalDateTime searchTime, PagingParams pagingParams
    ) {
        Member member = memberService.searchLoginMember(tokenPayload.getId());
        List<Challenge> challenges = challengeService.findAllByFilter(pagingParams);
        List<Cycle> cycles = cycleService.findInProgressByChallenges(searchTime, challenges);
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

    public ChallengeResponse findWithChallengerCount(
            TokenPayload tokenPayload, LocalDateTime searchTime, Long challengeId
    ) {
        Member member = memberService.searchLoginMember(tokenPayload.getId());
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> cycles = cycleService.findInProgressByChallenge(searchTime, challenge);
        ChallengingRecords challengingRecords = ChallengingRecords.from(cycles);
        return new ChallengeResponse(
                challenge,
                challengingRecords.countChallenger(challenge),
                challengingRecords.isChallenging(challenge, member)
        );
    }

    public List<ChallengeOfMineResponse> findAllByMeAndFilter(TokenPayload tokenPayload,
                                                              PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload.getId());
        ChallengingRecords challengingRecords = ChallengingRecords.from(
                cycleService.findAllByMemberAndFilter(member, pagingParams)
        );
        List<ChallengingRecord> sortedRecords = challengingRecords.sortByLatestProgressTime();

        ChallengingRecord cursorChallengingRecord = getCursorMemberChallenge(pagingParams, sortedRecords);
        List<ChallengingRecord> pagedMyChallengeHistories = CursorPaging.apply(
                sortedRecords, cursorChallengingRecord, pagingParams.getSize()
        );

        return pagedMyChallengeHistories.stream()
                .map(ChallengeOfMineResponse::new)
                .collect(toList());
    }

    private ChallengingRecord getCursorMemberChallenge(PagingParams pagingParams,
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
        List<Cycle> cycles = cycleService.findAllByChallengeIdAndMemberId(challengeId, tokenPayload.getId());
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
