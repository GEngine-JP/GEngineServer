package info.xiaomo.server.rpg.map;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.concurrent.ScheduledEventDispatcher;
import info.xiaomo.gengine.concurrent.queue.QueueDriver;
import info.xiaomo.gengine.map.Topography;
import info.xiaomo.gengine.utils.ExecutorUtil;
import info.xiaomo.server.rpg.config.tables.MapConfig;
import info.xiaomo.server.rpg.server.game.GameContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 地图工厂类
 *
 * @author zhangli 2017年6月6日 下午9:53:11
 */
public class MapFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapFactory.class);

    public static GameMap createMap(MapConfig mapConfig, int line) {
        String binaryFile =
                GameContext.getOption().getConfigDataPath()
                        + "map/"
                        + mapConfig.getId()
                        + "/mapdata.list";
        Topography topography = null;
        try {
            topography = new Topography(binaryFile, mapConfig.getWidth(), mapConfig.getHeight());
        } catch (Exception e) {
            LOGGER.error("地图:【{}】: 【{}】 创建失败", mapConfig.getId(), mapConfig.getName(), e);
        }

        GameMap map = new GameMap(topography, null);
        map.setTopography(topography);
        map.setLine(line);
        map.setName(mapConfig.getName());
        map.setCfgId(mapConfig.getId());
        map.setId(mapConfig.getId());
        map.setCanCross(mapConfig.getCancross() == 1);
        map.setReliveType(mapConfig.getReliveType());

        // 创建怪物
        initMonster(
                map,
                GameContext.getOption().getConfigDataPath()
                        + "map/"
                        + map.getCfgId()
                        + "/monsters.csv");
        // 创建NPC
        initNPC(map);

        initEvent(map);

        // 设置驱动器
        map.setDriver(
                new QueueDriver(
                        ExecutorUtil.COMMON_DRIVER_EXECUTOR,
                        mapConfig.getName() + "-" + (line),
                        mapConfig.getId(),
                        5000));
        // 是指事件分发器
        map.setEventDispatcher(
                new ScheduledEventDispatcher(map.getDriver(), map.getId(), map.getLine()));

        // 添加定时事件
        //        map.getEventDispatcher().addTimerEvent(new PlayerHeartEvent(map));
        //        map.getEventDispatcher().addTimerEvent(new MonsterHeartEvent(map));
        //        map.getEventDispatcher().addTimerEvent(new BufferHeartEvent(map));
        //        map.getEventDispatcher().addTimerEvent(new PetHeartEvent(map));
        //        map.getEventDispatcher().addTimerEvent(new ItemHeartEvent(map));

        // 启动场景驱动器
        ScheduledFuture<?> future =
                ExecutorUtil.EVENT_DISPATCHER_EXECUTOR.scheduleAtFixedRate(
                        map.getEventDispatcher(), 0, 100, TimeUnit.MILLISECONDS);
        map.getEventDispatcher().setFuture(future);

        return map;
    }

    public static void initMonster(GameMap map, String fileurl) {
        //        CSVData data = CSVUtil.read(fileurl, 0);
        //        for (Map<String, String> mobj : data.tableRows) {
        //            int mid = Cast.toInteger(mobj.get("mid"));
        //            int x = Cast.toInteger(mobj.get("born-x"));
        //            int y = Cast.toInteger(mobj.get("born-y"));
        //            Monster monster = MapObjectFactory.toCreateMonster(mid, 0);
        //            if (map.isInstance()) {
        //                monster.setRemoveAfterDie(true);
        //            }
        //            Point point = map.getPoint(x, y);
        //            monster.setBirthPoint(point);
        //            map.enterMonster(monster, point, false);
        //        }
    }

    public static void initNPC(GameMap map) {
        //        List<MapNpcConfig> mapNpcConfigList =
        //                ConfigDataManager.getInstance().getList(MapNpcConfig.class);
        //        for (MapNpcConfig npcConfig : mapNpcConfigList) {
        //            if (npcConfig.getMapid() == map.getCfgId()) {
        //                NPC player = MapObjectFactory.createNPC(npcConfig.getNpcid(),
        // npcConfig.getDir());
        //                if (player == null) {
        //                    continue;
        //                }
        //                player.setId(IDUtil.getId());
        //                player.setMapNpcId(npcConfig.getId());
        //                Point point = map.getPoint(npcConfig.getX(), npcConfig.getY());
        //                map.enterNpc(player, point);
        //            }
        //        }
    }

    public static void initEvent(GameMap map) {

        //        List<MapEventConfig> eventConfigList =
        //                ConfigDataManager.getInstance().getList(MapEventConfig.class);
        //        for (MapEventConfig eventConfig : eventConfigList) {
        //            if (eventConfig.getMapid() == map.getCfgId()) {
        //                int x = eventConfig.getX();
        //                int y = eventConfig.getY();
        //                Point point = map.getPoint(x, y);
        //                if (point == null) {
        //                    LOGGER.error(
        //                            "{}|{}|地图配置错误，未找到指定的事件点【x:{}, y:{}】",
        //                            map.getCfgId(),
        //                            map.getName(),
        //                            x,
        //                            y);
        //                    continue;
        //                }
        //                Map<Integer, List<Event>> eventMap = map.getPointEventMap();
        //
        //                List<Event> eventList = eventMap.get(point.getId());
        //                if (eventList == null) {
        //                    eventList = new ArrayList<>();
        //                    eventMap.put(point.getId(), eventList);
        //                }
        //
        //                List<Integer> pointIdList = new ArrayList<Integer>();
        //
        //                // 周围两格范围内都要增加事件(用同一个列表)
        //                int[] ps = GeomUtil.getPointRoundOffset(1);
        //                for (int index = 0; index < ps.length; index += 2) { // 这里面已经包含了当前点
        //                    int near = ((x + ps[index]) << 16) + y + ps[index + 1];
        //                    Point nearPoint = map.getPoint(near);
        //                    if (nearPoint == null) {
        //                        continue;
        //                    }
        //                    nearPoint.setHasEvent(true);
        //                    if (eventMap.get(near) == null) {
        //                        eventMap.put(near, eventList);
        //                    }
        //                    pointIdList.add(near);
        //                }
        //
        //                // 创建场景对象
        //                GroundEvent groundEvent =
        // MapObjectFactory.createEvent(eventConfig.getId());
        //                map.enterEvent(groundEvent, point);
        //
        //                boolean hasTransmitEvent = false;
        //                for (int eventId : eventConfig.getEventids()) {
        //                    EventConfig eventsConfig =
        //                            ConfigDataManager.getInstance().getById(EventConfig.class,
        // eventId);
        //                    if (eventsConfig == null) {
        //                        LOGGER.error("EventsConfig中找不到id为" + eventId + "的数据");
        //                        continue;
        //                    }
        //                    EventTrigger trigger =
        //                            EventFactory.getEventTrigger(
        //                                    eventsConfig.getTriggertype(),
        // eventsConfig.getTriggerparam());
        //                    EventExecutor executor =
        //                            EventFactory.getEventExecutor(
        //                                    eventsConfig.getType(), eventsConfig.getParam());
        //                    Event event = new Event(eventId, eventsConfig.getPriority(), trigger,
        // executor);
        //                    event.setGroundEventId(groundEvent.getId());
        //                    eventList.add(event);
        //
        //                    if (eventsConfig.getType() == EventExecutor.TRANSMIT) {
        //                        hasTransmitEvent = true;
        //                    }
        //                }
        //
        //                if (hasTransmitEvent) {
        //                    for (Integer pointId : pointIdList) {
        //                        Point p = map.getPoint(pointId);
        //                        if (p == null) {
        //                            continue;
        //                        }
        //                        p.setHasTransmitEvent(true);
        //                    }
        //                }
        //
        //                // 对优先级排序
        //                Collections.sort(eventList);
        //            }
        //        }
    }
}
