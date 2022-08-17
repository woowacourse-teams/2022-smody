package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PagingParams;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, PagingParams pagingParams) {
        return entityManager.createQuery(findAllFilterByQuery(
                memberId, challengeId, pagingParams))
                .setMaxResults(pagingParams.getDefaultSize())
                .getResultList();
    }

    private CriteriaQuery<Cycle> findAllFilterByQuery(
            Long memberId, Long challengeId, PagingParams pagingParams) {
        String filter = convertFilter(pagingParams.getFilter());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cycle> cycleCriteriaQuery = criteriaBuilder.createQuery(Cycle.class);
        Root<Cycle> cycleRoot = cycleCriteriaQuery.from(Cycle.class);
        Predicate equalsToMember = criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId);
        Predicate equalsToChallenge = criteriaBuilder.equal(cycleRoot.get("challenge").get("id"), challengeId);
        Predicate equalsToSuccess = criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase());
        Order orderByStartTime = criteriaBuilder.desc(cycleRoot.get(pagingParams.getSort()));
        Predicate overThanId = criteriaBuilder.greaterThan(cycleRoot.get("id"), pagingParams.getCursorId());

        if (filter.equals("success")) {
            return cycleCriteriaQuery.select(cycleRoot)
                    .where(criteriaBuilder.and(
                            equalsToMember, equalsToChallenge, equalsToSuccess, overThanId))
                    .orderBy(orderByStartTime);
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(criteriaBuilder.and(equalsToMember, equalsToChallenge, overThanId))
                .orderBy(orderByStartTime);
    }

    private String convertFilter(String filter) {
        if (filter == null) {
            return "";
        }
        return filter;
    }

    @Override
    public List<Cycle> findByMemberWithFilter(Long memberId, PagingParams pagingParams) {
        String convertedFilter = convertFilter(pagingParams.getFilter());
        return entityManager.createQuery(findByMemberWithFilterQuery(memberId, convertedFilter, pagingParams.getCursorId()))
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
                                    criteriaBuilder.notEqual(cycleRoot.get("challenge").get("id"), id),
                                    criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId),
                                    criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase())
                            )
                    );
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(
                        criteriaBuilder.and(
                                criteriaBuilder.notEqual(cycleRoot.get("challenge").get("id"), id),
                                criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId)
                        )
                );
    }
}
