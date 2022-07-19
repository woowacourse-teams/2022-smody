package com.woowacourse.smody.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
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
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;
    private final MemberRepository memberRepository;

    public List<ChallengeResponse> findAllWithChallengerCount(LocalDateTime searchTime, Pageable pageable) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        List<ChallengeResponse> responses = getResponsesOrderByCount(inProgressCycles);
        return PagingUtil.page(responses, pageable);
    }

    public List<ChallengeResponse> findAllWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime,
                                                              Pageable pageable) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        List<ChallengeResponse> responses = getResponsesOrderByCount(inProgressCycles).stream()
                .map(response -> response.changeInProgress(matchMember(inProgressCycles, tokenPayload, response.getChallengeId())))
                .collect(toList());
        return PagingUtil.page(responses, pageable);
    }

    private List<Cycle> searchInProgressCycles(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    private List<ChallengeResponse> getResponsesOrderByCount(List<Cycle> inProgressCycles) {
        return challengeRepository.findAll()
                .stream()
                .map(challenge -> new ChallengeResponse(challenge, countByChallenge(inProgressCycles, challenge)))
                .sorted((response1, response2) ->
                        Integer.compare(response2.getChallengerCount(), response1.getChallengerCount()))
                .collect(toList());
    }

    private boolean matchMember(List<Cycle> cycles, TokenPayload tokenPayload, Long challengeId) {
        return cycles.stream()
                .anyMatch(cycle -> cycle.matchChallenge(challengeId) && cycle.matchMember(tokenPayload.getId()));
    }

    private int countByChallenge(List<Cycle> cycles, Challenge challenge) {
        return (int) cycles.stream()
                .filter(cycle -> cycle.matchChallenge(challenge.getId()))
                .count();
    }

    public List<SuccessChallengeResponse> searchSuccessOfMine(TokenPayload tokenPayload, Pageable pageable) {
        Member member = searchMember(tokenPayload);
        List<Cycle> cycles = cycleRepository.findAllSuccessLatest(member);

        Map<Challenge, Long> groupedSize = cycles.stream()
                .collect(groupingBy(Cycle::getChallenge, Collectors.counting()));

        List<Challenge> pagedChallenges = PagingUtil.page(extractChallenges(cycles), pageable);
        return pagedChallenges.stream()
                .map(challenge -> new SuccessChallengeResponse(
                        challenge.getId(), challenge.getName(), groupedSize.get(challenge).intValue()))
                .collect(toList());
    }

    private List<Challenge> extractChallenges(List<Cycle> cycles) {
        return cycles.stream()
                .map(Cycle::getChallenge)
                .distinct()
                .collect(toList());
    }

    public ChallengeResponse findOneWithChallengerCount(LocalDateTime searchTime, Long challengeId) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        Challenge challenge = searchChallenge(challengeId);
        int count = countByChallenge(inProgressCycles, challenge);
        return new ChallengeResponse(challenge, count);
    }

    public ChallengeResponse findOneWithChallengerCount(TokenPayload tokenPayload, LocalDateTime searchTime, Long challengeId) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        Challenge challenge = searchChallenge(challengeId);
        int count = countByChallenge(inProgressCycles, challenge);
        return new ChallengeResponse(challenge, count, matchMember(inProgressCycles, tokenPayload, challengeId));
    }

    private Member searchMember(TokenPayload tokenPayload) {
        return memberRepository.findById(tokenPayload.getId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }

    private Challenge searchChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CHALLENGE));
    }
}
