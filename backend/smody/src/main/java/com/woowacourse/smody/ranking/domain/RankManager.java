package com.woowacourse.smody.ranking.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

    public final Map<Integer, Integer> ranksOfActivities;

    private RankManager(Map<Integer, Integer> ranksOfActivities) {
        this.ranksOfActivities = ranksOfActivities;
    }

    // TODO-이거도 사실은 정렬된게 들어온다는 전제가 있어야하는데 너무 쿼리에 의존적이게 되서 객체지향을 해치게 되는거 같다. 괜찮을까?
    public static RankManager of(List<RankingActivity> rankingActivitiesOrderByPointDesc) {
        Map<Integer, Integer> cache = new HashMap<>();
        for (int ranking = 0; ranking < rankingActivitiesOrderByPointDesc.size(); ranking++) {
            RankingActivity rankingActivity = rankingActivitiesOrderByPointDesc.get(ranking);
            cache.putIfAbsent(rankingActivity.getPoint(), ranking + 1);
        }
        return new RankManager(cache);
    }

    public Integer getRanking(RankingActivity rankingActivity) {
        return ranksOfActivities.get(rankingActivity.getPoint());
    }
}
