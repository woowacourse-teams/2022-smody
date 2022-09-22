package com.woowacourse.smody;

import static com.woowacourse.smody.cycle.domain.QCycle.cycle;
import static com.woowacourse.smody.cycle.domain.QCycleDetail.cycleDetail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataBatch {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public void setUp() {
        List<Cycle> cycles = queryFactory
                .selectFrom(cycle)
                .join(cycle.cycleDetails, cycleDetail)
                .fetchJoin()
                .distinct()
                .fetch();

        for (Cycle cycle : cycles) {
            List<CycleDetail> cycleDetails = cycle.getCycleDetails();
            for (int i = 1; i <= cycleDetails.size(); i++) {
                cycleDetails.get(i - 1).setProgress(Progress.values()[i]);
            }
        }
    }
}
