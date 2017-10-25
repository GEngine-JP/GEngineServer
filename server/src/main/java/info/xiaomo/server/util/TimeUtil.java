package info.xiaomo.server.util;

import info.xiaomo.server.server.GameContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xiaomo
 */
public class TimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 一分钟的毫秒时长
     */
    public static final long ONE_MINUTE_IN_MILLISECONDS = 60L * 1000;
    /**
     * 一小时的毫秒时长
     */
    public static final long ONE_HOUR_IN_MILLISECONDS = 60L * ONE_MINUTE_IN_MILLISECONDS;
    /**
     * 一天的毫秒时长
     */
    public static final long ONE_DAY_IN_MILLISECONDS = 24L * ONE_HOUR_IN_MILLISECONDS;
    /**
     * 一天的秒时长
     */
    public static final long ONE_DAY_IN_SECONDS = 24L * 60 * 60;

    /**
     * 1秒的时长
     */
    public static final long ONE_MILLS = 1000L;

    /**
     * 2015-02-23 12:12:12格式
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 判断两个时间是否是同一天
     *
     * @param sourceTime
     * @param targetTime
     * @return
     */
    public static boolean isSameDay(long sourceTime, long targetTime) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sourceTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long sourceTimeZero = calendar.getTimeInMillis();


        calendar.setTimeInMillis(targetTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long targetTimeZero = calendar.getTimeInMillis();

        return sourceTimeZero == targetTimeZero;
    }

    /**
     * 判断指定的时间是否是今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        return isSameDay(System.currentTimeMillis(), time);
    }

    /**
     * 获取两个时间的逻辑间隔天数,以源时间为基准,目标时间小于源时间则返回大于或等于天数，反之返回小于等于天数
     * <p>
     * 举例：sourceTime=今天凌晨0点0分1秒,targetTime=昨天晚上11点59分59秒,则返回1
     *
     * @param sourceTime
     * @param targetTime
     * @return
     */
    public static int getLogicIntervalDays(long sourceTime, long targetTime) {
        long source0ClockTime = getZeroClockTime(sourceTime);
        long target0ClockTime = getZeroClockTime(targetTime);

        return getRealIntervalDays(source0ClockTime, target0ClockTime);
    }

    /**
     * 获取两个时间的实际间隔天数
     *
     * @param sourceTime
     * @param targetTime
     * @return
     */
    public static int getRealIntervalDays(long sourceTime, long targetTime) {
        return (int) getIntervalTime(sourceTime, targetTime, ONE_DAY_IN_MILLISECONDS);
    }

    /**
     * 根据指定的时间单位获取相差的单位时间，如时间单位为一天的毫秒数则该函数跟{@link#getRealIntervalDays} 则是相同的效果
     *
     * @param sourceTime
     * @param targetTime
     * @param timeUnit   时间单位(毫秒)
     * @return
     */
    public static long getIntervalTime(long sourceTime, long targetTime, long timeUnit) {
        return (sourceTime - targetTime) / timeUnit;
    }

    /**
     * 获取在指定时间戳和指定小时，分钟，秒，毫秒数的时间
     *
     * @param time        时间戳
     * @param hour        小时(24小时制)
     * @param minute      分钟
     * @param second      秒
     * @param milliSecond 毫秒
     * @return
     */
    public static long getTimeInMillis(long time, int hour, int minute, int second, int milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定日期的时间戳
     *
     * @param year
     * @param month       从1开始
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @return
     */
    public static long getTimeInMillis(int year, int month, int day, int hour, int minute, int second,
                                       int milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
        return calendar.getTimeInMillis();
    }

    public static long getNowOfMills() {
        return System.currentTimeMillis();
    }

    public static int getNowOfSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int getNowOfMinutes() {
        return TimeUtil.getNowOfSeconds() / 60;
    }

    /**
     * 获取今日指定的时间
     *
     * @param hour        小时(24小时制)
     * @param minute      分钟
     * @param second      秒
     * @param milliSecond 毫秒
     * @return
     */
    public static long getTodayTime(int hour, int minute, int second, int milliSecond) {
        return getTimeInMillis(System.currentTimeMillis(), hour, minute, second, milliSecond);
    }

    /**
     * 获取指定时间的零点时间
     *
     * @param time
     * @return
     */
    public static long getZeroClockTime(long time) {
        return getTimeInMillis(time, 0, 0, 0, 0);
    }

    /**
     * 获取开服天数,开服首日算作第一天
     *
     * @return
     */
    public static int getOpenServerDay() {
        return getLogicIntervalDays(System.currentTimeMillis(), GameContext.getOpenDayZeroTime()) + 1;
    }

    /**
     * 获取合服天数，合服首日算作第一天
     *
     * @return
     */
    public static int getCombineServerDay() {
        if (!GameContext.isCombined()) {
            return 0;
        }
        return getLogicIntervalDays(System.currentTimeMillis(), GameContext.getCombineDayZeroTime()) + 1;
    }

    /**
     * 返回指定时间和格式的时间字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String getTimeString(long time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    /**
     * 从字符串中获取时间
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static long getTimeFromString(String timeStr, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(timeStr).getTime();
        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return Long.MIN_VALUE;
    }

    /**
     * 获取格式化的剩余时间
     * <p>
     * 例如:1天20小时5分0秒,20小时0分0秒,1秒
     *
     * @param leftTime
     * @return
     */
    public static String getLeftTimeString(long leftTime) {
        StringBuilder sb = new StringBuilder();
        // 获取剩余天数
        int day = (int) (leftTime / ONE_DAY_IN_MILLISECONDS);
        // 1天及以上的显示剩余天
        if (day > 0) {
            sb.append(day).append("天");
            leftTime -= (day * ONE_DAY_IN_MILLISECONDS);
        }
        int hour = (int) (leftTime / ONE_HOUR_IN_MILLISECONDS);
        // 1小时及以上或者前面显示了天数则后面需要小时
        if (hour > 0 || sb.length() > 0) {
            sb.append(hour).append("小时");
            leftTime -= (hour * ONE_HOUR_IN_MILLISECONDS);
        }
        int minute = (int) (leftTime / ONE_MINUTE_IN_MILLISECONDS);
        if (minute > 0 || sb.length() > 0) {
            sb.append(minute).append("分");
            leftTime -= (minute * ONE_MINUTE_IN_MILLISECONDS);
        }
        sb.append(leftTime / 1000).append("秒");
        return sb.toString();
    }

    /**
     * 判断今天是否为同一个月
     *
     * @return boolean
     */
    public static boolean isSameMouth(int oldMonth) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return month == oldMonth;
    }
}
