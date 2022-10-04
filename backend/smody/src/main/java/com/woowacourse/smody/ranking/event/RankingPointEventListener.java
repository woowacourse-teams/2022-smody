package com.woowacourse.smody.ranking.event;

import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.CycleProgressEvent;
import com.woowacourse.smody.ranking.service.RankingService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
public class RankingPointEventListener {

    @Value("#{${admin.staff}}")
    private List<Long> staffs;

    private final RankingService rankingService;
    private final TransactionTemplate transactionTemplate;
    
    @TransactionalEventListener
    @Async("asyncExecutor")
    public void handle(CycleProgressEvent event) {
        Cycle cycle = event.getCycle();
        Long memberId = cycle.getMember().getId();
        if (staffs.contains(memberId)) {
            return;
        }
        try {
            applyRankingPointWithTransaction(cycle);
        } catch (DataIntegrityViolationException e) {
            /*
            * 여러 사람이 동시에 랭킹 점수에 관한 이벤트를 발생했을 경우
            * unique 제약조건으로 예외가 발생한 이벤트를 새로운 트랜잭션으로 처리한다.
             */
            applyRankingPointWithTransaction(cycle);
        }
    }

    private void applyRankingPointWithTransaction(Cycle cycle) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                CycleDetail cycleDetail = cycle.getLatestCycleDetail();
                LocalDateTime progressTime = cycleDetail.getProgressTime();
                rankingService.findInProgressActivity(progressTime, cycle.getMember())
                        .forEach(activity -> activity.active(cycleDetail.getProgress()));
            }
        });
    }
}
