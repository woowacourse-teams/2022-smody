package com.woowacourse.smody.feed.repository;

import static com.querydsl.core.types.ExpressionUtils.as;
import static com.woowacourse.smody.challenge.domain.QChallenge.challenge;
import static com.woowacourse.smody.comment.domain.QComment.comment;
import static com.woowacourse.smody.cycle.domain.QCycle.cycle;
import static com.woowacourse.smody.cycle.domain.QCycleDetail.cycleDetail;
import static com.woowacourse.smody.member.domain.QMember.member;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.db_support.SortSelection;
import com.woowacourse.smody.feed.domain.Feed;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedDynamicRepositoryImpl implements FeedDynamicRepository {

    private static final Expression<Long> COMMENT_COUNT = as(
            JPAExpressions.select(comment.count())
                    .from(comment)
                    .where(comment.cycleDetail.eq(cycleDetail)),
            "commentCount"
    );

    private static final Expression<?>[] FEED_FIELDS = {
            cycleDetail.id, cycleDetail.progressImage, cycleDetail.description,
            cycleDetail.progressTime, cycleDetail.progress,
            member.id, member.picture, member.nickname,
            challenge.id, challenge.name,
            COMMENT_COUNT
    };

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Feed> findAll(PagingParams pagingParams) {
        OrderSpecifier<?>[] orderSpecifiers = SortSelection.findByParameter(pagingParams.getSort())
                .getOrderSpecifiers();

        Long cursorCycleDetailId = pagingParams.getCursorId();
        LocalDateTime cursorProgressTime = getCursorProgressTime(cursorCycleDetailId);

        return queryFactory
                .select(Projections.constructor(Feed.class, FEED_FIELDS))
                .from(cycleDetail)
                .join(cycleDetail.cycle, cycle)
                .join(cycle.member, member)
                .join(cycle.challenge, challenge)
                .where(DynamicQuery.builder()
                        .and(() -> cycleDetail.id.ne(cursorCycleDetailId))
                        .and(() -> cycleDetail.progressTime.loe(cursorProgressTime))
                        .build()
                )
                .orderBy(orderSpecifiers)
                .limit(pagingParams.getSize())
                .fetch();
    }

    private LocalDateTime getCursorProgressTime(Long cycleDetailId) {
        return queryFactory.select(cycleDetail.progressTime)
                .from(cycleDetail)
                .where(cycleDetail.id.eq(cycleDetailId))
                .fetchOne();
    }
}
