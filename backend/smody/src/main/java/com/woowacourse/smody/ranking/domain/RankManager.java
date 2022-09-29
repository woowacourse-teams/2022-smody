package com.woowacourse.smody.ranking.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

    public final Map<RankingActivity, Integer> activityRanks;

    private RankManager(Map<RankingActivity, Integer> activityRanks) {
        this.activityRanks = activityRanks;
    }

    public static RankManager rank(List<RankingActivity> activities) {
        Map<RankingActivity, Integer> activityRanks = new HashMap<>();
        int ranking = 1;
        for (int i = 0; i < activities.size(); i++) {
            RankingActivity now = activities.get(i);
            ranking = determineRank(activities, ranking, i);
            activityRanks.put(now, ranking);
        }
        return new RankManager(activityRanks);
    }

    private static int determineRank(List<RankingActivity> rankingActivities, int ranking, int i) {
        RankingActivity now = rankingActivities.get(i);
        if (i != 0 && !rankingActivities.get(i - 1).isDraw(now)) {
            ranking++;
        }
        return ranking;
    }

    public Integer getRanking(RankingActivity rankingActivity) {
        return activityRanks.get(rankingActivity);
    }
}
