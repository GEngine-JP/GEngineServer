package info.xiaomo.server.rpg.system.schedule;

import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.server.rpg.event.EventType;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

/**
 * @author xiaomo
 */
@Slf4j
public class ServerHeartTask implements Runnable {


    private int lastMinute;

    private int lastDay;


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
                EventUtil.fireEvent(EventType.SERVER_MINUTE_HEART);
            }

            //每天一次(零点过后至少20秒才执行)
            int delay = 20;
            if (day != lastDay && second > delay) {
                lastDay = day;
                EventUtil.fireEvent(EventType.SERVER_MIDNIGHT);
            }

            //每秒 一次
            EventUtil.fireEvent(EventType.SERVER_SECOND_HEART);

        } catch (Throwable e) {
            log.error("服务器心跳事件发生错误.", e);
        }

    }


}
