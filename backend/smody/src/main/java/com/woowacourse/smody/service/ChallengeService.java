package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.woowacourse.smody.repository.MemberRepository;
import com.woowacourse.smody.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CycleRepository cycleRepository;
    private final MemberRepository memberRepository;

    public List<ChallengeResponse> findAllWithChallengerCount(LocalDateTime searchTime, Pageable pageable) {
        List<Cycle> inProgressCycles = searchInProgressCycles(searchTime);
        List<ChallengeResponse> responses = challengeRepository.findAll()
                .stream()
                .map(challenge -> new ChallengeResponse(challenge, calculateCountByChallenge(inProgressCycles, challenge)))
                .sorted((response1, response2) -> Integer.compare(response2.getChallengerCount(), response1.getChallengerCount()))
                .collect(toList());
        return PagingUtil.page(responses, pageable);
    }

    private List<Cycle> searchInProgressCycles(LocalDateTime searchTime) {
        return cycleRepository.findAllByStartTimeIsAfter(searchTime.minusDays(Cycle.DAYS))
                .stream()
                .filter(cycle -> cycle.isInProgress(searchTime))
                .collect(toList());
    }

    private int calculateCountByChallenge(List<Cycle> cycles, Challenge challenge) {
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

    private Member searchMember(TokenPayload tokenPayload) {
        return memberRepository.findById(tokenPayload.getId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }
}
