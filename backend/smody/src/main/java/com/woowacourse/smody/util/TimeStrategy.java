package com.woowacourse.smody.util;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimeStrategy {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
