package com.woowacourse.smody.ranking.domain;

import com.woowacourse.smody.cycle.domain.Progress;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Point {

    FIRST(10, Progress.FIRST),
    SECOND(30, Progress.SECOND),
    SUCCESS(60, Progress.SUCCESS);

    private static final int ZERO = 0;

    private final Integer value;
    private final Progress progress;

    Point(Integer value, Progress progress) {
        this.value = value;
        this.progress = progress;
    }

    public static Integer calculate(Progress progress) {
        return Arrays.stream(values())
                .filter(point -> point.progress == progress)
                .map(Point::getValue)
                .findAny()
                .orElse(ZERO);
    }
}
