package com.woowacourse.smody.feed.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.smody.db_support.DynamicQuery;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.db_support.SortSelection;
import com.woowacourse.smody.feed.domain.Feed;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.woowacourse.smody.challenge.domain.QChallenge.challenge;
import static com.woowacourse.smody.comment.domain.QComment.comment;
import static com.woowacourse.smody.cycle.domain.QCycle.cycle;
import static com.woowacourse.smody.cycle.domain.QCycleDetail.cycleDetail;
import static com.woowacourse.smody.member.domain.QMember.member;

@RequiredArgsConstructor
public class FeedDynamicRepositoryImpl implements FeedDynamicRepository {

    private static final Expression<Long> COMMENT_COUNT = ExpressionUtils.as(
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
    public List<Feed> searchAll(PagingParams pagingParams) {
        OrderSpecifier<?>[] orders = SortSelection.findByParameter(pagingParams.getSort())
                .getOrderSpecifiers();

        Long cycleDetailId = pagingParams.getDefaultCursorId();
        LocalDateTime progressTime = getCursorProgressTime(cycleDetailId);

        return queryFactory
                .select(Projections.constructor(Feed.class, FEED_FIELDS))
                .from(cycleDetail)
                .join(cycleDetail.cycle, cycle)
                .join(cycle.member, member)
                .join(cycle.challenge, challenge)
                .where(DynamicQuery.builder()
                        .and(() -> cycleDetail.id.ne(cycleDetailId))
                        .and(() -> cycleDetail.progressTime.loe(progressTime))
                        .build()
                )
                .orderBy(orders)
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
