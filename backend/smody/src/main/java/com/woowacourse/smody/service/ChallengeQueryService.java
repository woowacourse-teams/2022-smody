package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeQueryService {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final CycleService cycleService;

    public List<ChallengesResponse> findAllWithChallengerCount(LocalDateTime searchTime, Pageable pageable) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengesResponse> responses = getResponsesOrderId(inProgressCycles);
        return PagingUtil.page(responses, pageable);
    }

    public List<ChallengesResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                              Pageable pageable) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengesResponse> responses = getResponsesOrderId(inProgressCycles).stream()
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

    private List<ChallengesResponse> getResponsesOrderId(Map<Challenge, List<Cycle>> inProgressCycles) {
        return challengeService.searchAll().stream().map(challenge ->
                        new ChallengesResponse(
                                challenge, inProgressCycles.getOrDefault(challenge, List.of()).size()))
                .sorted(Comparator.comparingLong(ChallengesResponse::getChallengeId))
                .collect(toList());
    }

    private boolean matchMember(Map<Challenge, List<Cycle>> inProgressCycles,
                                TokenPayload tokenPayload,
                                Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        return inProgressCycles.getOrDefault(challenge, List.of()).stream()
                .anyMatch(cycle -> cycle.matchMember(tokenPayload.getId()));
    }

    public List<SuccessChallengeResponse> searchSuccessOfMine(TokenPayload tokenPayload, Pageable pageable) {
        Member member = memberService.search(tokenPayload);
        List<Cycle> cycles = cycleService.findSuccessLatestByMember(member);

        Map<Challenge, Long> groupedSize = cycles.stream()
                .collect(groupingBy(Cycle::getChallenge, Collectors.counting()));

        List<Challenge> pagedChallenges = PagingUtil.page(extractChallenges(cycles), pageable);
        return pagedChallenges.stream()
                .map(challenge -> new SuccessChallengeResponse(
                        challenge.getId(), challenge.getName(),
                        groupedSize.get(challenge).intValue(),
                        challenge.getEmoji()
                )).collect(toList());
    }

    private List<Challenge> extractChallenges(List<Cycle> cycles) {
        return cycles.stream()
                .map(Cycle::getChallenge)
                .distinct()
                .collect(toList());
    }

    public List<ChallengersResponse> findAllChallengers(Long challengeId) {
        Challenge challenge = challengeService.search(challengeId);
        List<Cycle> inProgressCycle = cycleService.searchInProgress(LocalDateTime.now())
                .stream()
                .filter(cycle -> cycle.matchChallenge(challenge.getId()))
                .collect(toList());
        return inProgressCycle.stream()
                .map(cycle -> new ChallengersResponse(
                        cycle.getMember().getId(), cycle.getMember().getNickname(),
                        cycle.getProgress().getCount(), cycle.getMember().getPicture(),
                        cycle.getMember().getIntroduction()))
                .collect(toList());
    }

    public List<ChallengesResponse> searchByName(String name) {
        validateSearchingName(name);
        List<Challenge> challenges = challengeService.searchByNameContaining(name);
        Map<Challenge, List<Cycle>> inProgressChallenge = groupByChallenge(cycleService.searchInProgress(LocalDateTime.now()));
        Map<Challenge, Integer> matchedChallenge = new HashMap<>();
        challenges.stream()
                .filter(inProgressChallenge::containsKey)
                .forEach(challenge -> matchedChallenge.put(challenge, inProgressChallenge.get(challenge).size()));
        challenges.stream()
                .filter(challenge -> !inProgressChallenge.containsKey(challenge))
                .forEach(challenge -> matchedChallenge.put(challenge, 0));
        return matchedChallenge.keySet().stream()
                .map(challenge -> new ChallengesResponse(
                        challenge.getId(), challenge.getName(), matchedChallenge.get(challenge),
                        false, challenge.getEmoji()))
                .sorted(Comparator.comparingLong(ChallengesResponse::getChallengeId))
                .collect(toList());
    }

    public List<ChallengesResponse> searchByName(TokenPayload tokenPayload, String name) {
        validateSearchingName(name);
        List<Challenge> challenges = challengeService.searchByNameContaining(name);
        Map<Challenge, List<Cycle>> inProgressChallenge = groupByChallenge(cycleService.searchInProgress(LocalDateTime.now()));
        Map<Challenge, Integer> matchedChallenge = new HashMap<>();
        challenges.stream()
                .filter(inProgressChallenge::containsKey)
                .forEach(challenge -> matchedChallenge.put(challenge, inProgressChallenge.get(challenge).size()));
        challenges.stream()
                .filter(challenge -> !inProgressChallenge.containsKey(challenge))
                .forEach(challenge -> matchedChallenge.put(challenge, 0));
        return matchedChallenge.keySet().stream()
                .map(challenge -> new ChallengesResponse(
                        challenge.getId(), challenge.getName(), matchedChallenge.get(challenge),
                        matchMember(inProgressChallenge, tokenPayload, challenge.getId()), challenge.getEmoji()))
                .sorted(Comparator.comparingLong(ChallengesResponse::getChallengeId))
                .collect(toList());
    }

    private void validateSearchingName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_SEARCH_NAME);
        }
    }
}
