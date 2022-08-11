package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
@Repository
public class DynamicChallengeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaQuery<Challenge> createDynamicQuery(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Challenge> challengeCriteriaQuery = criteriaBuilder.createQuery(Challenge.class);
        Root<Challenge> challengeRoot = challengeCriteriaQuery.from(Challenge.class);
        if (name == null) {
            return challengeCriteriaQuery.select(challengeRoot);
        }
        validateSearchingName(name);
        return challengeCriteriaQuery.select(challengeRoot)
                .where(criteriaBuilder.like(challengeRoot.get("name"), "%" + name + "%"));
    }

    public List<Challenge> searchAll(String name) {
        return entityManager.createQuery(createDynamicQuery(name)).getResultList();
    }

    private void validateSearchingName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_SEARCH_NAME);
        }
    }
}
