package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.util.PagingUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeQueryService {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;

    public List<ChallengeTabResponse> findAllWithChallengerCount(LocalDateTime searchTime, Pageable pageable,
                                                                 String searchingName) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengeTabResponse> responses = getResponsesOrderId(inProgressCycles, searchingName);
        return PagingUtil.page(responses, pageable);
    }

    public List<ChallengeTabResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                                 Pageable pageable, String searchingName) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengeTabResponse> responses = getResponsesOrderId(inProgressCycles, searchingName).stream()
                .map(response -> response.changeInProgress(
                        matchMember(inProgressCycles, tokenPayload, response.getChallengeId()))).collect(toList());
        return PagingUtil.page(responses, pageable);
    }

    public ChallengeResponse findOneWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        Challenge challenge = challengeService.search(challengeId);
        int count = inProgressCycles.getOrDefault(challenge, List.of()).size();
        return new ChallengeResponse(challenge, count);
    }

    public ChallengeResponse findOneWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
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
                                                           String searchingName) {
        return challengeService.searchAll(searchingName).stream().map(challenge ->
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

    public List<ChallengeOfMineResponse> searchOfMine(TokenPayload tokenPayload, Pageable pageable) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.findByMember(member);

        Map<Challenge, Long> groupedSize = cycles.stream()
                .filter(Cycle::isSuccess)
                .collect(groupingBy(Cycle::getChallenge, Collectors.counting()));

        Map<Challenge, LocalDateTime> groupByProgressTime = groupByChallenge(cycles).entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .map(Cycle::getLatestProgressTime)
                        .sorted(Comparator.reverseOrder())
                        .findFirst()
                        .get()));

        List<Challenge> latestChallenges = groupByProgressTime.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(toList());

        List<Challenge> pagedChallenges = PagingUtil.page(latestChallenges, pageable);
        return pagedChallenges.stream()
                .map(challenge -> new ChallengeOfMineResponse(
                        challenge, groupedSize.get(challenge).intValue()
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

    public ChallengeHistoryResponse findOneWithMine(TokenPayload tokenPayload, Long challengeId) {
        List<Cycle> cycles = cycleService.findAllByChallengeIdAndMemberId(challengeId, tokenPayload.getId()).stream()
                .filter(cycle -> !cycle.isInProgress(LocalDateTime.now()))
                .collect(toList());
        if (cycles.isEmpty()) {
            throw new BusinessException(ExceptionData.NOT_FOUND_CHALLENGE);
        }
        Challenge challenge = cycles.get(0).getChallenge();
        int successCount = (int) cycles.stream()
                .filter(Cycle::isSuccess)
                .count();
        int cycleDetailCount = cycles.stream()
                .mapToInt(cycle -> cycle.getCycleDetails().size())
                .sum();
        return new ChallengeHistoryResponse(challenge, successCount, cycleDetailCount);
    }
}
