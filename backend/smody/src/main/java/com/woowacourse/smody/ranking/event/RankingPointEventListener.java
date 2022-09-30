package com.woowacourse.smody.ranking.event;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.ranking.service.RankingService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RankingPointEventListener {

    private final RankingService rankingService;

    @Transactional
    @TransactionalEventListener
    @Async("asyncExecutor")
    public void handle(CycleProgressEvent event) {
        Cycle cycle = event.getCycle();
        CycleDetail cycleDetail = cycle.getLatestCycleDetail();
        LocalDateTime progressTime = cycleDetail.getProgressTime();

        rankingService.findInProgressActivity(progressTime, cycle.getMember())
                .forEach(activity -> activity.active(cycleDetail.getProgress()));
    }
}
