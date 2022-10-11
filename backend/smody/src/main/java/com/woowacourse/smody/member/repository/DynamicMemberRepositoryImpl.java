package com.woowacourse.smody.member.repository;

import static com.woowacourse.smody.member.domain.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DynamicMemberRepositoryImpl implements DynamicMemberRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findAll(PagingParams pagingParams) {
        String searchWord = pagingParams.getFilter();
        BooleanBuilder conditions = DynamicQuery.builder()
                .and(() -> member.nickname.contains(searchWord))
                .and(() -> member.id.gt(pagingParams.getCursorId()))
                .build();

        return queryFactory
                .selectFrom(member)
                .where(conditions)
                .limit(pagingParams.getSize())
                .fetch();
    }
}
