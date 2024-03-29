package com.woowacourse.smody.db_support;

import static com.woowacourse.smody.cycle.domain.QCycle.cycle;
import static com.woowacourse.smody.cycle.domain.QCycleDetail.cycleDetail;
import static com.woowacourse.smody.ranking.domain.QRankingPeriod.rankingPeriod;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.Arrays;
import org.springframework.data.domain.Sort;

public enum SortSelection {

    DEFAULT("") {
        @Override
        public Sort getSort() {
            return Sort.unsorted();
        }

        @Override
        public OrderSpecifier<?>[] getOrderSpecifiers() {
            return new OrderSpecifier[0];
        }
    },

    FEED_LATEST("latest") {
        @Override
        public Sort getSort() {
            return Sort.by("progressTime").descending().and(Sort.by("id").descending());
        }

        @Override
        public OrderSpecifier<?>[] getOrderSpecifiers() {
            return new OrderSpecifier[]{
                    cycleDetail.progressTime.desc(),
                    cycleDetail.id.desc()
            };
        }
    },

    CYCLE_LATEST("cycle_latest") {
        @Override
        public Sort getSort() {
            return Sort.by("startTime").descending().and(Sort.by("id").descending());
        }

        @Override
        public OrderSpecifier<?>[] getOrderSpecifiers() {
            return new OrderSpecifier[]{
                    cycle.startTime.desc(),
                    cycle.id.desc()
            };
        }
    },

    RANKING_PERIOD("startDate:desc") {
        @Override
        public Sort getSort() {
            return Sort.by("startDate").descending();
        }

        @Override
        public OrderSpecifier<?>[] getOrderSpecifiers() {
            return new OrderSpecifier[]{
                    rankingPeriod.startDate.desc()
            };
        }
    },

    RANDOM("random") {
        @Override
        public Sort getSort() {
            throw new BusinessException(ExceptionData.NOT_FOUND_SORT);
        }

        @Override
        public OrderSpecifier<?>[] getOrderSpecifiers() {
            return new OrderSpecifier[]{
                    NumberExpression.random().asc()
            };
        }
    };

    private final String parameter;

    SortSelection(String parameter) {
        this.parameter = parameter;
    }

    public abstract Sort getSort();

    public abstract OrderSpecifier<?>[] getOrderSpecifiers();

    public static SortSelection findByParameter(String parameter) {
        return Arrays.stream(SortSelection.values())
                .filter(customOrder -> customOrder.parameter.equals(parameter))
                .findAny()
                .orElse(DEFAULT);
    }
}
