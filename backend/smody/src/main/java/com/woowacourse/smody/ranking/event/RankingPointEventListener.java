package com.woowacourse.smody.ranking.event;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.ranking.domain.RankingActivity;
import com.woowacourse.smody.ranking.service.RankingService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RankingPointEventListener {

    private final RankingService rankingService;

    @Transactional
    public void handle(CycleProgressEvent event) {
        Cycle cycle = event.getCycle();
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        LocalDateTime progressTime = cycleDetail.getProgressTime();
        List<RankingActivity> activities = rankingService.findInProgressActivity(progressTime, cycle.getMember());
        for (RankingActivity activity : activities) {
            activity.active(cycleDetail.getProgress());
        }
    }
}
