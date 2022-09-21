package com.woowacourse.smody.ui.admin;

import lombok.Getter;

@Getter
public enum LogLevel {

    DEBUG("DEBUG", "aqua"),
    INFO("INFO", "lime"),
    WARN("WARN", "yellow"),
    ERROR("ERROR", "red"),
    ;

    private final String text;
    private final String color;

    LogLevel(String text, String color) {
        this.text = text;
        this.color = color;
    }
}
