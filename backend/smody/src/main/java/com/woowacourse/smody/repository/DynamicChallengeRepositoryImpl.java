package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import org.springframework.stereotype.Repository;

@Repository
public class DynamicChallengeRepositoryImpl implements DynamicChallengeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Challenge> searchAll(String name, Long cursorId, Integer size) {
        return entityManager.createQuery(createDynamicQuery(name, cursorId))
                .setMaxResults(size)
                .getResultList();
    }

    private CriteriaQuery<Challenge> createDynamicQuery(String name, Long cursorId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Challenge> challengeCriteriaQuery = criteriaBuilder.createQuery(Challenge.class);
        Root<Challenge> challengeRoot = challengeCriteriaQuery.from(Challenge.class);
        Predicate partialMatch = criteriaBuilder.like(challengeRoot.get("name"), "%" + name + "%");
        Predicate overThanLastId = criteriaBuilder.greaterThan(challengeRoot.get("id"), cursorId);
        if (name == null) {
            return challengeCriteriaQuery.select(challengeRoot)
                    .where(overThanLastId);
        }
        validateSearchingName(name);
        return challengeCriteriaQuery.select(challengeRoot)
                .where(criteriaBuilder.and(partialMatch, overThanLastId));
    }

    private void validateSearchingName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_SEARCH_NAME);
        }
    }
}
