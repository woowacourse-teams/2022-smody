package com.woowacourse.smody.ranking.domain;

import java.time.Period;

public enum Duration {

    WEEK(Period.ofWeeks(1));

    private final Period period;

    Duration(Period period) {
        this.period = period;
    }

    public long getDays() {
        return period.getDays();
    }
}
