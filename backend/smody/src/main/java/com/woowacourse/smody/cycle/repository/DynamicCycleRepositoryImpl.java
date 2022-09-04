package com.woowacourse.smody.cycle.repository;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.cycle.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, LocalDateTime lastTime, PagingParams pagingParams) {
        return entityManager.createQuery(findAllFilterByQuery(
                memberId, challengeId, lastTime, pagingParams))
                .setMaxResults(pagingParams.getDefaultSize())
                .getResultList();
    }

    private CriteriaQuery<Cycle> findAllFilterByQuery(
            Long memberId, Long challengeId, LocalDateTime lastTime, PagingParams pagingParams) {
        String filter = convertFilter(pagingParams.getFilter());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cycle> cycleCriteriaQuery = criteriaBuilder.createQuery(Cycle.class);
        Root<Cycle> cycleRoot = cycleCriteriaQuery.from(Cycle.class);
        Predicate equalsToMember = criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId);
        Predicate equalsToChallenge = criteriaBuilder.equal(cycleRoot.get("challenge").get("id"), challengeId);
        Predicate equalsToSuccess = criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase());
        Order orderByStartTime = criteriaBuilder.desc(cycleRoot.get(convertSort(pagingParams.getSort())));
        Predicate notEqualId = criteriaBuilder.notEqual(cycleRoot.get("id"), pagingParams.getDefaultCursorId());
        Predicate olderThanLastTime = criteriaBuilder.lessThanOrEqualTo(cycleRoot.get("startTime"), lastTime);
        if (filter.equals("success")) {
            return cycleCriteriaQuery.select(cycleRoot)
                    .where(criteriaBuilder.and(
                            equalsToMember, equalsToChallenge, equalsToSuccess, notEqualId, olderThanLastTime))
                    .orderBy(orderByStartTime);
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(criteriaBuilder.and(equalsToMember, equalsToChallenge, notEqualId, olderThanLastTime))
                .orderBy(orderByStartTime);
    }

    private String convertFilter(String filter) {
        if (filter == null) {
            return "";
        }
        return filter;
    }

    private String convertSort(String sort) {
        if (sort == null) {
            return "startTime";
        }
        return sort;
    }

    @Override
    public List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams) {
        String convertedFilter = convertFilter(pagingParams.getFilter());
        return entityManager.createQuery(findByMemberWithFilterQuery(memberId, convertedFilter, pagingParams.getDefaultCursorId()))
                .getResultList();
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
                                    criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase())
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
