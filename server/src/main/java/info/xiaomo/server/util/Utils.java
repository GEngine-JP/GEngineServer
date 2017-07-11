package com.sh.game.util;

/**
 * Created by 张力 on 2017/6/28.
 */
public class Utils {
    public static String getStackTrace() {
        return getStackTrace(1, 10);
    }

    public static String getStackTrace(int start, int stop) {
        if (start > stop) {
            throw new IllegalArgumentException("start > stop");
        }
        StringBuilder builder = new StringBuilder((stop - start + 1) * 50);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < start + 2) {
            return builder.toString();
        }
        appendElement(builder, stackTrace[start + 1]);
        for (int i = start + 2; i < stop + 1 && i < stackTrace.length; i++) {
            builder.append("<=");
            appendElement(builder, stackTrace[i]);
        }
        return builder.toString();
    }

    private static void appendElement(StringBuilder builder, StackTraceElement element) {
        String className = element.getClassName();
        String methodName = element.getMethodName();
        int index = className.lastIndexOf('.');
        builder.append(className.substring(index + 1))
                .append('.')
                .append(methodName)
                .append(':')
                .append(element.getLineNumber());
    }
}
