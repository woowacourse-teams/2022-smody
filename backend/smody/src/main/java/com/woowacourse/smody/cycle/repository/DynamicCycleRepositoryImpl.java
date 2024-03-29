package com.woowacourse.smody.cycle.repository;

import static com.querydsl.core.types.ExpressionUtils.as;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.woowacourse.smody.challenge.domain.QChallenge.challenge;
import static com.woowacourse.smody.cycle.domain.QCycle.cycle;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.challenge.domain.ChallengingRecord;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.domain.QCycle;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cycle> findAllByMemberAndChallengeAndFilter(Long memberId,
                                                            Long challengeId,
                                                            PagingParams pagingParams) {
        return queryFactory
                .selectFrom(cycle)
                .where(buildDynamicQuery(memberId, challengeId, pagingParams))
                .orderBy(cycle.startTime.desc())
                .limit(pagingParams.getSize())
                .fetch();
    }

    private BooleanBuilder buildDynamicQuery(Long memberId, Long challengeId, PagingParams pagingParams) {
        return DynamicQuery.builder()
                .and(() -> cycle.member.id.eq(memberId))
                .and(() -> cycle.challenge.id.eq(challengeId))
                .and(() -> cycle.id.ne(pagingParams.getCursorId()))
                .and(() -> cycle.startTime.loe(findCursorStartTime(pagingParams)))
                .and(() -> cycle.progress.eq(Progress.from(pagingParams.getFilter())
                        .orElse(null)))
                .build();
    }

    private LocalDateTime findCursorStartTime(PagingParams pagingParams) {
        return queryFactory
                .select(cycle.startTime)
                .from(cycle)
                .where(cycle.id.eq(pagingParams.getCursorId()))
                .fetchOne();
    }

    @Override
    public List<Cycle> findAllByMemberAndFilter(Long memberId, PagingParams pagingParams) {
        return queryFactory
                .selectFrom(cycle)
                .join(cycle.challenge).fetchJoin()
                .leftJoin(cycle.cycleDetails).fetchJoin()
                .where(DynamicQuery.builder()
                        .and(() -> cycle.member.id.eq(memberId))
                        .and(() -> cycle.progress.eq(Progress.from(pagingParams.getFilter())
                                .orElse(null)))
                        .build()
                ).distinct()
                .fetch();
    }

    @Override
    public List<ChallengingRecord> findAllChallengingRecordByMemberAfterTime(Long memberId,
                                                                             LocalDateTime time) {
        return queryFactory
                .select(challengingRecordConstructor())
                .from(cycle)
                .join(cycle.challenge, challenge).fetchJoin()
                .where(DynamicQuery.builder()
                        .and(() -> cycle.member.id.eq(memberId))
                        .and(() -> cycle.startTime.after(time))
                        .build())
                .fetch();
    }

    private ConstructorExpression<ChallengingRecord> challengingRecordConstructor() {
        return Projections.constructor(
                ChallengingRecord.class,
                cycle,
                successCountSubQuery());
    }

    private Expression<Long> successCountSubQuery() {
        QCycle subCycle = new QCycle("subCycle");
        return as(
                JPAExpressions.select(count(subCycle.id))
                        .from(subCycle)
                        .where(new BooleanBuilder()
                                .and(subCycle.challenge.eq(cycle.challenge))
                                .and(subCycle.progress.eq(Progress.SUCCESS))
                                .and(subCycle.member.eq(cycle.member))
                        ),
                "successCount");
    }
}
