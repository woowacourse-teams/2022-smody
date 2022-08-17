package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;

import com.woowacourse.smody.domain.PagingParams;
import org.springframework.data.domain.Pageable;

public class DynamicCycleRepositoryImpl implements DynamicCycleRepository {

    public static final long FIRST_SEARCH = -1L;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cycle> findAllFilterBy(
            Long memberId, Long challengeId, String filter, Long lastCycleId, Pageable pageable) {
        String convertedFilter = convertFilter(filter);
        Long convertedLastCycleIndex = convertLastCycleIndex(lastCycleId);
        LocalDateTime convertedStartTime = convertStartTime(convertedLastCycleIndex);
        return entityManager.createQuery(findAllFilterByQuery(
                memberId, challengeId, convertedFilter, convertedLastCycleIndex, convertedStartTime))
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private CriteriaQuery<Cycle> findAllFilterByQuery(
            Long memberId, Long challengeId, String filter, Long lastCycleId, LocalDateTime startTime) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cycle> cycleCriteriaQuery = criteriaBuilder.createQuery(Cycle.class);
        Root<Cycle> cycleRoot = cycleCriteriaQuery.from(Cycle.class);
        Predicate equalsToMember = criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId);
        Predicate equalsToChallenge = criteriaBuilder.equal(cycleRoot.get("challenge").get("id"), challengeId);
        Predicate equalsToSuccess = criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase());
        Order orderByStartTime = criteriaBuilder.desc(cycleRoot.get("startTime"));
        Predicate notEqualsToId = criteriaBuilder.notEqual(cycleRoot.get("id"), lastCycleId);
        Predicate overThanStartTime = criteriaBuilder.lessThanOrEqualTo(cycleRoot.get("startTime"), startTime);

        if (filter.equals("success")) {
            return cycleCriteriaQuery.select(cycleRoot)
                    .where(criteriaBuilder.and(
                            equalsToMember, equalsToChallenge, equalsToSuccess, notEqualsToId, overThanStartTime))
                    .orderBy(orderByStartTime);
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(criteriaBuilder.and(equalsToMember, equalsToChallenge, notEqualsToId, overThanStartTime))
                .orderBy(orderByStartTime);
    }

    private String convertFilter(String filter) {
        if (filter == null) {
            return "";
        }
        return filter;
    }

    private Long convertLastCycleIndex(Long lastCycleId) {
        if (lastCycleId == null) {
            return FIRST_SEARCH;
        }
        return lastCycleId;
    }

    private LocalDateTime convertStartTime(Long lastCycleId) {
        if (lastCycleId <= 0) {
            return LocalDateTime.now();
        }
        Cycle cycle = entityManager.find(Cycle.class, lastCycleId);
        return cycle.getStartTime();
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
                                    criteriaBuilder.greaterThan(cycleRoot.get("challenge").get("id"), id),
                                    criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId),
                                    criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase())
                            )
                    );
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(
                        criteriaBuilder.and(
                                criteriaBuilder.greaterThan(cycleRoot.get("challenge").get("id"), id),
                                criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId)
                        )
                );
    }
}
