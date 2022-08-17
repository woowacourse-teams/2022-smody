package com.woowacourse.smody.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.dto.ChallengeOfMineRequest;
import com.woowacourse.smody.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.ChallengeTabResponse;
import com.woowacourse.smody.dto.ChallengersResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.util.PagingUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<ChallengeOfMineResponse> searchOfMineWithFilter(TokenPayload tokenPayload,
                                                                ChallengeOfMineRequest challengeOfMineRequest) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.findByMemberWithFilter(member, challengeOfMineRequest.getFilter());

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
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(toList());

        List<Challenge> pagedChallenges = PagingUtil.page(latestChallenges, challengeOfMineRequest.toPageRequest());
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
