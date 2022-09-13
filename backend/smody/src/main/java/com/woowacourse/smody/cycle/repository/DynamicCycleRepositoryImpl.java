package com.woowacourse.smody.cycle.repository;

import static com.woowacourse.smody.cycle.domain.QCycle.cycle;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cycle> findAllByMemberAndChallenge(Long memberId, Long challengeId, PagingParams pagingParams) {
        return queryFactory
                .selectFrom(cycle)
                .where(buildDynamicQuery(memberId, challengeId, pagingParams))
                .orderBy(cycle.startTime.desc())
                .limit(pagingParams.getDefaultSize())
                .fetch();
    }

    private BooleanBuilder buildDynamicQuery(Long memberId, Long challengeId, PagingParams pagingParams) {
        return DynamicQuery.builder()
                .and(() -> cycle.member.id.eq(memberId))
                .and(() -> cycle.challenge.id.eq(challengeId))
                .and(() -> cycle.id.ne(pagingParams.getDefaultCursorId()))
                .and(() -> cycle.startTime.loe(findCursorStartTime(pagingParams)))
                .and(() -> cycle.progress.eq(Progress.from(pagingParams.getFilter())
                        .orElse(null)))
                .build();
    }

    private LocalDateTime findCursorStartTime(PagingParams pagingParams) {
        return queryFactory
                .select(cycle.startTime)
                .from(cycle)
                .where(cycle.id.eq(pagingParams.getDefaultCursorId()))
                .fetchOne();
    }

    @Override
    public List<Cycle> findAllByMember(Long memberId, PagingParams pagingParams) {
        return queryFactory
                .selectFrom(cycle)
                .join(cycle.challenge).fetchJoin()
                .leftJoin(cycle.cycleDetails).fetchJoin()
                .where(DynamicQuery.builder()
                        .and(() -> cycle.member.id.eq(memberId))
                        .and(() -> cycle.progress.eq(Progress.from(pagingParams.getFilter())
                                .orElse(null)))
                        .build()
                )
                .distinct()
                .fetch();
    }
}
