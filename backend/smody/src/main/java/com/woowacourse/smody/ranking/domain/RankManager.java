package com.woowacourse.smody.ranking.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

    public final Map<Integer, Integer> activityRanks;

    private RankManager(Map<Integer, Integer> activityRanks) {
        this.activityRanks = activityRanks;
    }

    public static RankManager rank(List<RankingActivity> activities) {
        Map<Integer, Integer> cache = new HashMap<>();
        for (int ranking = 0; ranking < activities.size(); ranking++) {
            RankingActivity rankingActivity = activities.get(ranking);
            cache.putIfAbsent(rankingActivity.getPoint(), ranking + 1);
        }
        return new RankManager(cache);
    }

    public Integer getRanking(RankingActivity rankingActivity) {
        return activityRanks.get(rankingActivity.getPoint());
    }
}
