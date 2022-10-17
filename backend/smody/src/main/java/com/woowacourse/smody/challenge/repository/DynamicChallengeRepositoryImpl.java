package com.woowacourse.smody.challenge.repository;

import static com.woowacourse.smody.challenge.domain.QChallenge.challenge;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.db_support.SortSelection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DynamicChallengeRepositoryImpl implements DynamicChallengeRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Challenge> findAllByFilter(PagingParams pagingParams) {
        String searchWord = pagingParams.getFilter();
        BooleanBuilder conditions = DynamicQuery.builder()
                .and(() -> challenge.name.contains(searchWord))
                .and(() -> challenge.id.gt(pagingParams.getCursorId()))
                .build();

        OrderSpecifier<?>[] orderSpecifiers = SortSelection.findByParameter(pagingParams.getSort())
                .getOrderSpecifiers();

        return queryFactory
                .selectFrom(challenge)
                .where(conditions)
                .orderBy(orderSpecifiers)
                .limit(pagingParams.getSize())
                .fetch();
    }
}
