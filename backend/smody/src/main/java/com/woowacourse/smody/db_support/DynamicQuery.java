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
            // 위의 예외가 터지면 null이 들어왔다는 뜻이다.
            // null이면 where 쿼리에 and 조건을 넣지 않기 위해 예외를 무시했다.
        }
        return this;
    }

    public BooleanBuilder build() {
        return booleanBuilder;
    }
}
