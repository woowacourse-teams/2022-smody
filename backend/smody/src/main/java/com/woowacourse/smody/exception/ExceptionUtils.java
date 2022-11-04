package com.woowacourse.smody.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static String extractStackTrace(Exception exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        return String.valueOf(stackTrace);
    }
}
