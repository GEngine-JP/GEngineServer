package info.xiaomo.server.util;

import info.xiaomo.core.util.Symbol;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/6/28 19:56
 * desc  : 堆栈信息
 * Copyright(©) 2017 by xiaomo.
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
        int index = className.lastIndexOf(Symbol.DIAN);
        builder.append(className.substring(index + 1))
                .append(Symbol.DIAN)
                .append(methodName)
                .append(Symbol.MAOHAO)
                .append(element.getLineNumber());
    }
}
