package com.woowacourse.smody.challenge.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.domain.MemberChallenge;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeQueryService {

    private static final TokenPayload NOT_LOGIN = new TokenPayload(0L);

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;

    public List<ChallengeTabResponse> findAllWithChallengerCount(LocalDateTime searchTime, PagingParams pagingParams) {
        return findAllWithChallengerCount(NOT_LOGIN, searchTime, pagingParams);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                                 PagingParams pagingParams) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<Challenge> challenges = challengeService.searchAll(pagingParams);
        return getChallengeTabResponses(challenges, inProgressCycles, tokenPayload);
    }

    private List<ChallengeTabResponse> getChallengeTabResponses(List<Challenge> challenges,
                                                                Map<Challenge, List<Cycle>> inProgressCycles,
                                                                TokenPayload tokenPayload) {
        return challenges.stream()
                .map(challenge -> new ChallengeTabResponse(
                        challenge,
                        getChallengerCount(inProgressCycles, challenge),
                        isChallenging(inProgressCycles, tokenPayload, challenge.getId())
                )).collect(toList());
    }

    public ChallengeResponse findWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        return findWithChallengerCount(NOT_LOGIN, searchTime, challengeId);
    }

    public ChallengeResponse findWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                     Long challengeId) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        Challenge challenge = challengeService.search(challengeId);
        return new ChallengeResponse(
                challenge,
                getChallengerCount(inProgressCycles, challenge),
                isChallenging(inProgressCycles, tokenPayload, challengeId)
        );
    }

    private Map<Challenge, List<Cycle>> groupByChallenge(List<Cycle> cycles) {
        return cycles.stream()
                .collect(groupingBy(Cycle::getChallenge));
    }

    private boolean isChallenging(Map<Challenge, List<Cycle>> inProgressCycles,
                                  TokenPayload tokenPayload,
                                  Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        return getCyclesByChallenge(inProgressCycles, challenge).stream()
                .anyMatch(cycle -> cycle.matchMember(tokenPayload.getId()));
    }

    private List<Cycle> getCyclesByChallenge(Map<Challenge, List<Cycle>> inProgressCycles, Challenge challenge) {
        return inProgressCycles.getOrDefault(challenge, List.of());
    }

    private int getChallengerCount(Map<Challenge, List<Cycle>> inProgressCycles, Challenge challenge) {
        return getCyclesByChallenge(inProgressCycles, challenge).size();
    }

    public List<ChallengeOfMineResponse> searchOfMine(TokenPayload tokenPayload,
                                                      PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.findAllByMember(member, pagingParams);
        List<MemberChallenge> memberChallenges = sortByLatestProgressTime(MemberChallenge.from(cycles));

        MemberChallenge cursorMemberChallenge = getCursorMemberChallenge(pagingParams, memberChallenges);
        List<MemberChallenge> pagedMemberChallenges = CursorPaging.apply(
                memberChallenges, cursorMemberChallenge, pagingParams.getDefaultSize()
        );

        return pagedMemberChallenges.stream()
                .map(ChallengeOfMineResponse::new)
                .collect(toList());
    }

    private List<MemberChallenge> sortByLatestProgressTime(List<MemberChallenge> memberChallenges) {
        return memberChallenges.stream()
                .sorted(Comparator.comparing(MemberChallenge::getLatestProgressTime).reversed()
                        .thenComparing(memberChallenge -> memberChallenge.getChallenge().getId()))
                .collect(toList());
    }

    private MemberChallenge getCursorMemberChallenge(PagingParams pagingParams,
                                                     List<MemberChallenge> memberChallenges) {
        return challengeService.findById(pagingParams.getDefaultCursorId())
                .map(cursor -> extractMatchChallenge(memberChallenges, cursor))
                .orElse(null);
    }

    private MemberChallenge extractMatchChallenge(List<MemberChallenge> memberChallenges, Challenge lastChallenge) {
        return memberChallenges.stream()
                .filter(memberChallenge -> memberChallenge.getChallenge().equals(lastChallenge))
                .findAny()
                .orElse(null);
    }

    public List<ChallengersResponse> findAllChallengers(Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> inProgressCycle = cycleService.searchInProgress(LocalDateTime.now())
                .stream()
                .filter(cycle -> cycle.matchChallenge(challenge.getId()))
                .collect(toList());
        return inProgressCycle.stream()
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
