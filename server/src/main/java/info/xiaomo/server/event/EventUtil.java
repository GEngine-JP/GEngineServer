package info.xiaomo.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 19:24
 * desc  : 事件公共方法
 * Copyright(©) 2017 by xiaomo.
 */
public class EventUtil {

    /**
     * 游戏启动就初始化的监听者列表
     */
    private final static Map<EventType, List<IListener>> preparedListeners = new HashMap<>();

    /**
     * 游戏运行中动态添加的监听者列表
     */
    private static ConcurrentHashMap<EventType, ConcurrentHashMap<IDynamicListener, Integer>> dynamicListeners = new ConcurrentHashMap<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(EventUtil.class);

    static void addListener(IListener listener, EventType type) {
        List<IListener> listenerList = preparedListeners.computeIfAbsent(type, k -> new ArrayList<>());
        listenerList.add(listener);
    }

    public static void remvoeListener(IDynamicListener listener, EventType type) {
        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if (map == null) {
            return;
        }
        map.remove(listener);
    }

    public static void addListener(IDynamicListener listener, EventType type) {
        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            ConcurrentHashMap<IDynamicListener, Integer> exist = dynamicListeners.putIfAbsent(type, map);
            if (exist != null) {
                map = exist;
            }
        }
        map.put(listener, null);
    }


    /**
     * 执行事件
     * @param type type
     * @param obj obj
     */
    public static void executeEvent(EventType type, Object obj) {

        List<IListener> listenerList = preparedListeners.get(type);
        if (listenerList != null) {
            for (IListener listener : listenerList) {
                try {
                    listener.update(type, obj);
                } catch (Exception e) {
                    LOGGER.error("事件执行错误", e);
                }
            }
        }

        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if (map != null) {
            for (IDynamicListener dynamicListener : map.keySet()) {
                try {
                    dynamicListener.update(type, obj);
                } catch (Exception e) {
                    LOGGER.error("事件执行错误", e);
                }
            }
        }

    }

}
