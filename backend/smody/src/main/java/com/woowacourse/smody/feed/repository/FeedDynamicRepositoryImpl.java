package com.woowacourse.smody.feed.repository;

import static com.woowacourse.smody.comment.domain.QComment.comment;
import static com.woowacourse.smody.cycle.domain.QCycleDetail.cycleDetail;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.common.PagingParams;
import com.woowacourse.smody.common.SortSelection;
import com.woowacourse.smody.feed.domain.Feed;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedDynamicRepositoryImpl implements FeedDynamicRepository {

    private static final Expression<Long> COMMENT_COUNT = ExpressionUtils.as(
            JPAExpressions.select(comment.count())
                    .from(comment)
                    .where(comment.cycleDetail.eq(cycleDetail)),
            "commentCount"
    );

    private static final Expression<?>[] FEED_FIELDS = {
            cycleDetail.id, cycleDetail.progressImage, cycleDetail.description, cycleDetail.progressTime,
            cycleDetail.cycle.member.id, cycleDetail.cycle.member.picture, cycleDetail.cycle.member.nickname,
            cycleDetail.cycle.challenge.id, cycleDetail.cycle.challenge.name,
            COMMENT_COUNT
    };

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Feed> searchAll(PagingParams pagingParams) {
        BooleanBuilder filters = getBooleanBuilder(pagingParams);
        OrderSpecifier<?>[] orders = SortSelection.findByParameter(pagingParams.getSort())
                .getOrderSpecifiers();

        return queryFactory
                .select(Projections.constructor(Feed.class, FEED_FIELDS))
                .from(cycleDetail)
                .where(filters)
                .orderBy(orders)
                .limit(pagingParams.getSize())
                .fetch();
    }

    private BooleanBuilder getBooleanBuilder(PagingParams pagingParams) {
        BooleanBuilder builder = new BooleanBuilder();
        Long cycleDetailId = pagingParams.getDefaultCursorId();
        LocalDateTime progressTime = getCursorProgressTime(cycleDetailId);

        if (cycleDetailId != null) {
            builder.and(cycleDetail.id.ne(cycleDetailId));
        }

        if (progressTime != null) {
            builder.and(cycleDetail.progressTime.loe(progressTime));
        }
        return builder;
    }

    private LocalDateTime getCursorProgressTime(Long cycleDetailId) {
        return queryFactory.select(cycleDetail.progressTime)
                .from(cycleDetail)
                .where(cycleDetail.id.eq(cycleDetailId))
                .fetchOne();
    }
}
