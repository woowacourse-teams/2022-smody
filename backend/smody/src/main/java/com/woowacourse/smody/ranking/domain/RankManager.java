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
            add(cache, ranking, activities.get(ranking));
        }
        return new RankManager(cache);
    }

    private static void add(Map<Integer, Integer> cache, int ranking, RankingActivity activity) {
        Integer point = activity.getPoint();
        if (!cache.containsKey(point)) {
            cache.put(point, ranking + 1);
        }
    }

    public Integer getRanking(RankingActivity rankingActivity) {
        return activityRanks.get(rankingActivity.getPoint());
    }
}
