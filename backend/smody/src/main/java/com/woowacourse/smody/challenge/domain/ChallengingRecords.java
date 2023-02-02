package com.woowacourse.smody.challenge.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.member.domain.Member;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChallengingRecords {

    private final Map<Challenge, List<ChallengingRecord>> values;

    private ChallengingRecords(Map<Challenge, List<ChallengingRecord>> challengingRecords) {
        this.values = challengingRecords;
    }

    public static ChallengingRecords create() {
        return new ChallengingRecords(new HashMap<>());
    }

    public void record(List<Cycle> cycles) {
        Map<Challenge, List<Cycle>> byChallenge = cycles.stream()
                .collect(groupingBy(Cycle::getChallenge));
        for (Challenge challenge : byChallenge.keySet()) {
            values.put(challenge, makeRecords(byChallenge, challenge));
        }
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
        return Optional.ofNullable(values.get(challenge))
                .orElse(List.of());
    }

    public boolean isChallenging(Challenge challenge, Member member) {
        return findByChallenge(challenge).stream()
                .anyMatch(challengingRecord -> challengingRecord.match(member));
    }

    public List<ChallengingRecord> sortByLatestProgressTime() {
        return values.values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ChallengingRecord::getLatestProgressTime).reversed()
                        .thenComparing(challengingRecord -> challengingRecord.getChallenge().getId()))
                .collect(toList());
    }

    public List<Challenge> getChallengesOrderByChallenger() {
        return values.keySet()
                .stream()
                .sorted(Comparator.comparing(challenge -> values.get(challenge).size()).reversed())
                .collect(toList());
    }
}
