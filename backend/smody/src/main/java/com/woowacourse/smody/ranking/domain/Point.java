package com.woowacourse.smody.ranking.domain;

import lombok.Getter;

@Getter
public enum Point {

    FIRST(10),
    SECOND(30),
    SUCCESS(60)
    ;


    private final Integer value;

    Point(Integer value) {
        this.value = value;
    }
}
