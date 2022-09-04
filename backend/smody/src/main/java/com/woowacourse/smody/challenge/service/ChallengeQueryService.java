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

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;

    public List<ChallengeTabResponse> findAllWithChallengerCount(LocalDateTime searchTime, PagingParams pagingParams) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        return getResponsesOrderId(inProgressCycles, pagingParams);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                                 PagingParams pagingParams) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        return getResponsesOrderId(inProgressCycles, pagingParams).stream()
                .map(response -> response.changeInProgress(
                        matchMember(inProgressCycles, tokenPayload, response.getChallengeId()))).collect(toList());
    }

    public ChallengeResponse findWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        Challenge challenge = challengeService.search(challengeId);
        int count = inProgressCycles.getOrDefault(challenge, List.of()).size();
        return new ChallengeResponse(challenge, count);
    }

    public ChallengeResponse findWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                     Long challengeId) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        Challenge challenge = challengeService.search(challengeId);
        int count = inProgressCycles.getOrDefault(challenge, List.of()).size();
        return new ChallengeResponse(challenge, count, matchMember(inProgressCycles, tokenPayload, challengeId));
    }

    private Map<Challenge, List<Cycle>> groupByChallenge(List<Cycle> cycles) {
        return cycles.stream()
                .collect(groupingBy(Cycle::getChallenge));
    }

    private List<ChallengeTabResponse> getResponsesOrderId(Map<Challenge, List<Cycle>> inProgressCycles,
                                                           PagingParams pagingParams) {
        return challengeService.searchAll(
                        pagingParams.getFilter(),
                        pagingParams.getDefaultCursorId(),
                        pagingParams.getDefaultSize()
                ).stream().map(challenge ->
                        new ChallengeTabResponse(
                                challenge, inProgressCycles.getOrDefault(challenge, List.of()).size()))
                .sorted(Comparator.comparingLong(ChallengeTabResponse::getChallengeId))
                .collect(toList());
    }

    private boolean matchMember(Map<Challenge, List<Cycle>> inProgressCycles,
                                TokenPayload tokenPayload,
                                Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        return inProgressCycles.getOrDefault(challenge, List.of()).stream()
                .anyMatch(cycle -> cycle.matchMember(tokenPayload.getId()));
    }

    public List<ChallengeOfMineResponse> searchOfMineWithFilter(TokenPayload tokenPayload,
                                                                PagingParams pagingParams) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.findAllByMember(member, pagingParams);
        List<MemberChallenge> memberChallenges = MemberChallenge.from(cycles)
                .stream()
                .sorted(Comparator.comparing(MemberChallenge::getLatestProgressTime).reversed()
                        .thenComparing(memberChallenge -> memberChallenge.getChallenge().getId()))
                .collect(toList());

        MemberChallenge cursorChallenge = getCursorMemberChallenge(pagingParams, memberChallenges);
        List<MemberChallenge> pagedChallenges = CursorPaging.apply(memberChallenges, cursorChallenge,
                pagingParams.getDefaultSize());

        return pagedChallenges.stream()
                .map(memberChallenge -> new ChallengeOfMineResponse(memberChallenge.getChallenge(),
                        memberChallenge.getSuccessCount()))
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

    private LocalDateTime getLatestProgressTimeOfChallenge(Long challengeId, Long memberId) {
        List<Cycle> lastChallengeCycles = cycleService.findAllByChallengeIdAndMemberId(challengeId, memberId);
        return lastChallengeCycles.stream()
                .map(Cycle::getLatestProgressTime)
                .max(Comparator.naturalOrder())
                .orElse(LocalDateTime.now());
    }
}
