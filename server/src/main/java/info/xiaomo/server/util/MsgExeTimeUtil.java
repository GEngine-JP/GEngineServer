package info.xiaomo.server.util;

import info.xiaomo.gameCore.base.concurrent.HandlerFilter;
import info.xiaomo.gameCore.base.tuple.TwoTuple;
import info.xiaomo.server.server.MessageExeTimeStatisticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 田小军 on 2016/12/24 0024.
 */
public class MsgExeTimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgExeTimeUtil.class);

    public class ThreadExeTime {
        public long allExeTime;
        public ConcurrentHashMap<Integer, Long> timeMapMax = new ConcurrentHashMap<>();
        public ConcurrentHashMap<Integer, Long> timeMapAll = new ConcurrentHashMap<>();
    }

    private static final MsgExeTimeUtil INSTANCE = new MsgExeTimeUtil();

    private static final HandlerFilter filer = new MessageExeTimeStatisticsHandler();

    private static ConcurrentHashMap<Long, ThreadExeTime> timeMap = new ConcurrentHashMap<>();

    private static boolean open = false;

    public static HandlerFilter getFiler() {
        return filer;
    }

    public static ConcurrentHashMap<Long, ThreadExeTime> getTimeMap() {
        return timeMap;
    }

    public static boolean isOpen() {
        return open;
    }

    public static void setOpen(boolean open) {
        MsgExeTimeUtil.open = open;
    }

    public static MsgExeTimeUtil getInstance() {
        return INSTANCE;
    }

    public ThreadExeTime newThreadExeTime() {
        return new ThreadExeTime();
    }

    public static void clear() {
        timeMap.clear();
    }

    public static Long getAllExeTime(Long mapKey) {
        ThreadExeTime threadExeTime = timeMap.get(mapKey);
        if (threadExeTime == null) {
            return 0L;
        }
        return threadExeTime.allExeTime;
    }

    public static Long getTimeExeMax(long mapKey, int msgId) {
        ThreadExeTime threadExeTime = timeMap.get(mapKey);
        if (threadExeTime == null) {
            return 0L;
        }
        return threadExeTime.timeMapMax.get(msgId);
    }

    public static Long getTimeExeAll(long mapKey, int msgId) {
        ThreadExeTime threadExeTime = timeMap.get(mapKey);
        if (threadExeTime == null) {
            return 0L;
        }
        return threadExeTime.timeMapAll.get(msgId);
    }

    /**
     * 获取所有消息的次数，按照发送量排序
     *
     * @param mapKey:mapId + lineId
     * @param sortType:    1: 消息最大执行时间 2:消息总的执行时间
     * @param num          获取排序前多少的消息
     * @return
     */
    public static List<TwoTuple<Integer, Long>> getTimeTop(Long mapKey, int sortType, int num) {
        if (sortType != 1 && sortType != 2) {
            return null;
        }

        ThreadExeTime threadExeTime = timeMap.get(mapKey);
        if (threadExeTime == null) {
            return null;
        }

        List<TwoTuple<Integer, Long>> ret = new ArrayList<>();
        for (Integer id : threadExeTime.timeMapMax.keySet()) {
            if (sortType == 1) {
                ret.add(new TwoTuple<>(id, threadExeTime.timeMapMax.get(id)));
            } else {
                ret.add(new TwoTuple<>(id, threadExeTime.timeMapAll.get(id)));
            }
        }

        ret.sort((o1, o2) -> Long.compare(o2.second, o1.second));
        if (num >= ret.size()) {
            return ret;
        } else {
            return ret.subList(0, num);
        }
    }

}
