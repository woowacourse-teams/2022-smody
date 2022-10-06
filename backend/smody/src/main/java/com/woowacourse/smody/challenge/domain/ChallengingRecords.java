package com.woowacourse.smody.challenge.domain;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.member.domain.Member;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ChallengingRecords {

    private final Map<Challenge, List<ChallengingRecord>> value;

    private ChallengingRecords(Map<Challenge, List<ChallengingRecord>> value) {
        this.value = value;
    }

    public static ChallengingRecords from(List<Cycle> cycles) {
        Map<Challenge, List<Cycle>> byChallenge = cycles.stream()
                .collect(groupingBy(Cycle::getChallenge));

        Map<Challenge, List<ChallengingRecord>> value = new HashMap<>();
        for (Challenge challenge : byChallenge.keySet()) {
            value.put(challenge, makeRecords(byChallenge, challenge));
        }
        return new ChallengingRecords(value);
    }

    private static List<ChallengingRecord> makeRecords(Map<Challenge, List<Cycle>> byChallenge, Challenge challenge) {
        Map<Member, List<Cycle>> byMember = byChallenge.get(challenge).stream()
                .collect(groupingBy(Cycle::getMember));
        return byMember.values().stream()
                .map(ChallengingRecord::new)
                .collect(Collectors.toList());
    }

    public int countChallenger(Challenge challenge) {
        return findByChallenge(challenge).size();
    }

    private List<ChallengingRecord> findByChallenge(Challenge challenge) {
        return Optional.ofNullable(value.get(challenge))
                .orElse(List.of());
    }

    public boolean isChallenging(Challenge challenge, Member member) {
        return findByChallenge(challenge).stream()
                .anyMatch(challengingRecord -> challengingRecord.match(member));
    }

    public List<ChallengingRecord> sortByLatestProgressTime() {
        return value.values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ChallengingRecord::getLatestProgressTime).reversed()
                        .thenComparing(memberChallenge -> memberChallenge.getChallenge().getId()))
                .collect(toList());
    }
}
