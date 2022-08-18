package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PagingParams;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

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
        LocalDateTime latestTime = generateBaseTime(pagingParams.getDefaultCursorId(), tokenPayload.getId());

        List<Cycle> cycles = cycleService.findByMemberWithFilter(member, pagingParams);

        Map<Challenge, Integer> groupedSize = groupByChallenge(cycles).entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .filter(Cycle::isSuccess)
                        .mapToInt(cycle -> 1)
                        .sum()));

        Map<Challenge, LocalDateTime> groupByProgressTime = groupByChallenge(cycles).entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .map(Cycle::getLatestProgressTime).max(Comparator.naturalOrder())
                        .orElseThrow(() -> new BusinessException(ExceptionData.NO_CERTED_CHALLENGE))));

        List<Challenge> latestChallenges = groupByProgressTime.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isBefore(latestTime) || entry.getValue().isEqual(latestTime))
                .sorted(Map.Entry.<Challenge, LocalDateTime>comparingByValue().reversed().thenComparing(entry -> entry.getKey().getId()))
                .map(Map.Entry::getKey)
                .collect(toList());

//        List<Challenge> futureChallenges = groupByProgressTime.entrySet()
//                .stream()
//                .filter(entry -> entry.getValue().isAfter(latestTime))
//                .sorted(Map.Entry.<Challenge, LocalDateTime>comparingByValue().reversed().thenComparing(entry -> entry.getKey().getId()))
//                .map(Map.Entry::getKey)
//                .collect(toList());
//
//        latestChallenges.addAll(futureChallenges);


        if (pagingParams.getDefaultCursorId() <= 0) {
            List<Challenge> pagedChallenges = latestChallenges.subList(0, Math.min(latestChallenges.size(), pagingParams.getDefaultSize()));
            return pagedChallenges.stream()
                    .map(challenge -> new ChallengeOfMineResponse(
                            challenge, groupedSize.get(challenge)
                    )).collect(toList());
        }

        Challenge lastChallenge = challengeService.search(pagingParams.getCursorId());
        int idx = latestChallenges.indexOf(lastChallenge);

        List<Challenge> pagedChallenges = latestChallenges.subList(idx + 1, Math.min(latestChallenges.size(), idx + 1 + pagingParams.getDefaultSize()));

        return pagedChallenges.stream()
                .map(challenge -> new ChallengeOfMineResponse(
                        challenge, groupedSize.get(challenge)
                )).collect(toList());
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

    private LocalDateTime generateBaseTime(Long challengeId, Long memberId) {
        List<Cycle> lastChallengeCycles = cycleService.findAllByChallengeIdAndMemberId(
                challengeId, memberId);
        if (lastChallengeCycles.isEmpty()) {
            return LocalDateTime.now();
        }
        return lastChallengeCycles.stream()
                .sorted(Comparator.comparing(Cycle::getLatestProgressTime)
                        .reversed())
                .findFirst()
                .get()
                .getLatestProgressTime();
    }
}
