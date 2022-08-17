package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        if (lastCycleId < 0) {
            return LocalDateTime.now();
        }
        Cycle cycle = entityManager.find(Cycle.class, lastCycleId);
        return cycle.getStartTime();
    }

    @Override
    public List<Cycle> findByMemberWithFilter(Long memberId, String filter) {
        String convertedFilter = convertFilter(filter);
        return entityManager.createQuery(findByMemberWithFilterQuery(memberId, convertedFilter))
                .getResultList();
    }

    private CriteriaQuery<Cycle> findByMemberWithFilterQuery(
            Long memberId, String filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cycle> cycleCriteriaQuery = criteriaBuilder.createQuery(Cycle.class);
        Root<Cycle> cycleRoot = cycleCriteriaQuery.from(Cycle.class);
        Predicate equalsToMember = criteriaBuilder.equal(cycleRoot.get("member").get("id"), memberId);
        Predicate equalsToSuccess = criteriaBuilder.equal(cycleRoot.get("progress").as(String.class), filter.toUpperCase());

        if (filter.equals("success")) {
            return cycleCriteriaQuery.select(cycleRoot)
                    .where(criteriaBuilder.and(
                            equalsToMember, equalsToSuccess));
        }
        return cycleCriteriaQuery.select(cycleRoot)
                .where(equalsToMember);
    }
}
