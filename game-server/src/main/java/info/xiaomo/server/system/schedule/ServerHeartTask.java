package info.xiaomo.server.system.schedule;

import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.EventUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @author xiaomo
 */
public class ServerHeartTask implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(ServerHeartTask.class);

    private int lastMinute = 0;

    private int lastDay = 0;


    public ServerHeartTask() {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        this.lastMinute = minute;
        this.lastDay = day;
    }


    @Override
    public void run() {
        try {

            Calendar calendar = Calendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_YEAR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            //每分 一次
            if (minute != lastMinute) {
                lastMinute = minute;
                EventUtil.fireEvent(EventType.SERVER_MINUTE_HEART, null);
            }

            //每天一次(零点过后至少20秒才执行)
            int delay = 20;
            if (day != lastDay && second > delay) {
                lastDay = day;
                EventUtil.fireEvent(EventType.SERVER_MIDNIGHT, null);
            }

            //每秒 一次
            EventUtil.fireEvent(EventType.SERVER_SECOND_HEART, null);

        } catch (Throwable e) {
            LOGGER.error("服务器心跳事件发生错误.", e);
        }

    }


}
