package com.woowacourse.smody.db_support;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.function.Supplier;

public class DynamicQuery {

    private final BooleanBuilder booleanBuilder;

    private DynamicQuery() {
        this.booleanBuilder = new BooleanBuilder();
    }

    public static DynamicQuery builder() {
        return new DynamicQuery();
    }

    public DynamicQuery and(Supplier<BooleanExpression> expressionSupplier) {
        try {
            booleanBuilder.and(expressionSupplier.get());
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return this;
    }

    public BooleanBuilder build() {
        return booleanBuilder;
    }
}
