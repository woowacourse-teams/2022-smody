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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
    public List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams) {
        String convertedFilter = convertFilter(pagingParams.getFilter());
        return entityManager.createQuery(
                        findByMemberWithFilterQuery(memberId, convertedFilter, pagingParams.getDefaultCursorId()))
                .getResultList();
    }

    private String convertFilter(String filter) {
        if (filter == null) {
            return "";
        }
        return filter;
    }

    private CriteriaQuery<Cycle> findByMemberWithFilterQuery(
            Long memberId, String filter, Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cycle> cycleCriteriaQuery = criteriaBuilder.createQuery(Cycle.class);
        Root<Cycle> cycleRoot = cycleCriteriaQuery.from(Cycle.class);

        if (filter.equals("success")) {
            return cycleCriteriaQuery.select(cycleRoot)
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId),
                                    criteriaBuilder.equal(cycleRoot.get("progress").as(String.class),
                                            filter.toUpperCase())
                            )
                    );
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId)
                        )
                );
    }
}
