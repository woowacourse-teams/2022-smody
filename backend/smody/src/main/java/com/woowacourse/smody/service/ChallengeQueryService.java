package com.woowacourse.smody.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.util.PagingUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public List<ChallengesResponse> findAllWithChallengerCount(LocalDateTime searchTime, Pageable pageable) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengesResponse> responses = getResponsesOrderByCount(inProgressCycles);
        return PagingUtil.page(responses, pageable);
    }

    public List<ChallengesResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                              Pageable pageable) {
        Map<Challenge, List<Cycle>> inProgressCycles = groupByChallenge(cycleService.searchInProgress(searchTime));
        List<ChallengesResponse> responses = getResponsesOrderByCount(inProgressCycles).stream()
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

    private List<ChallengesResponse> getResponsesOrderByCount(Map<Challenge, List<Cycle>> inProgressCycles) {
        return challengeService.searchAll().stream().map(challenge ->
                        new ChallengesResponse(
                                challenge, inProgressCycles.getOrDefault(challenge, List.of()).size()))
                .sorted((response1, response2) -> Integer.compare(response2.getChallengerCount(),
                        response1.getChallengerCount()))
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
}
