package com.woowacourse.smody.ranking.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankManager {

    public final Map<Integer, Integer> ranksOfActivities;

    private RankManager(Map<Integer, Integer> ranksOfActivities) {
        this.ranksOfActivities = ranksOfActivities;
    }

    public static RankManager of(List<RankingActivity> rankingActivities) {
        List<RankingActivity> sortedRankinActivities = sortByPointDesc(rankingActivities);
        Map<Integer, Integer> cache = new HashMap<>();
        for (int ranking = 0; ranking < sortedRankinActivities.size(); ranking++) {
            RankingActivity rankingActivity = sortedRankinActivities.get(ranking);
            cache.putIfAbsent(rankingActivity.getPoint(), ranking + 1);
        }
        return new RankManager(cache);
    }

    private static List<RankingActivity> sortByPointDesc(final List<RankingActivity> rankingActivities) {
        List<RankingActivity> sortedRankinActivities = rankingActivities.stream()
                .sorted(Comparator.comparing(RankingActivity::getPoint).reversed())
                .collect(Collectors.toList());
        return sortedRankinActivities;
    }

    public Integer getRanking(RankingActivity rankingActivity) {
        return ranksOfActivities.get(rankingActivity.getPoint());
    }
}
