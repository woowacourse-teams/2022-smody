package com.woowacourse.smody.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    public static String createIssueBody(Exception exception) {
        StringWriter issueBody = new StringWriter();
        exception.printStackTrace(new PrintWriter(issueBody));
        return String.valueOf(issueBody);
    }
}
