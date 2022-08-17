package com.woowacourse.smody.repository;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public enum SortSelection {

    DEFAULT("") {
        @Override
        public Sort getSort() {
            return Sort.unsorted();
        }
    },
    FEED_LATEST("latest") {
        @Override
        public Sort getSort() {
            return Sort.by("progressTime").descending().and(Sort.by("id").descending());
        }
    },

    CYCLE_LATEST("cycle_latest") {
        @Override
        public Sort getSort() {
            return Sort.by("startTime").descending().and(Sort.by("id").descending());
        }
    };

    private final String parameter;

    SortSelection(String parameter) {
        this.parameter = parameter;
    }

    public abstract Sort getSort();

    public static SortSelection findByParameter(String parameter) {
        return Arrays.stream(SortSelection.values())
                .filter(customOrder -> customOrder.parameter.equals(parameter))
                .findAny()
                .orElse(DEFAULT);
    }
}
