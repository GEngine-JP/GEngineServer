package info.xiaomo.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 由于事件都是预先注册好的，所以这里不考虑多线程问题，不允许在游戏运行过成中动态添加观察者
 *
 * @author 张力
 * @date 2014-12-6 上午11:19:00
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

    public static void fireEvent(EventType type, Object obj) {

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

    public static void fireEvent(EventType type) {
        fireEvent(type, null);
    }
}
