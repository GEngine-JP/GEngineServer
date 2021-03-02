package info.xiaomo.server.rpg.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.aoi.TowerAOI;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.gengine.utils.MapUtil;
import info.xiaomo.server.rpg.config.ConfigDataManager;
import info.xiaomo.server.rpg.config.tables.MapConfig;
import info.xiaomo.server.rpg.db.DataCenter;
import info.xiaomo.server.rpg.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 地图管理类
 *
 * @author zhangli 2017年6月6日 下午9:51:45
 */

/** @author zhangli 2017年6月6日 下午9:52:33 */
public class MapManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    private final Map<Long, GameMap> maps = new ConcurrentHashMap<>();

    private static final MapManager INSTANCE = new MapManager();

    public static MapManager getInstance() {
        return INSTANCE;
    }

    public Map<Long, GameMap> getMaps() {
        return maps;
    }

    /** 初始化地图 */
    public void initMaps() {

        List<MapConfig> mapList = ConfigDataManager.getInstance().getConfigs(MapConfig.class);
        for (MapConfig mapConfig : mapList) {

            if (mapConfig.getForbidden() == 1) {
                LOGGER.warn("跳过需要屏蔽的地图:[{}]-> [{}]", mapConfig.getId(), mapConfig.getName());
                continue;
            }

            if (mapConfig.getId() > 10000) {
                continue;
            }
            int line = mapConfig.getLine();
            if (line < 1) line = 1;
            for (int i = 1; i <= line; i++) { // 分线
                GameMap map = MapFactory.createMap(mapConfig, line);
                // 放进缓存
                maps.put(MapUtil.getMapKey(map.getId(), map.getLine()), map);
            }
        }
    }

    public GameMap removeMap(int mapId, int line) {
        return maps.remove(MapUtil.getMapKey(mapId, line));
    }

    public GameMap getMap(int mapId, int line) {
        return maps.get(MapUtil.getMapKey(mapId, line));
    }

    public GameMap getMap(IMapObject obj) {
        return getMap(obj.getMapId(), obj.getLine());
    }

    public GameMap getMap(long id) {
        return maps.get(id);
    }

    public void addMap(GameMap map) {
        maps.put(MapUtil.getMapKey(map.getId(), map.getLine()), map);
    }

    /**
     * 玩家登陆地图
     *
     * @param player
     */
    public void loginMap(Player player) {

        if (player.isLoginMap()) {
            LOGGER.error("重复登录场景:{},{}", player.getId(), player.getName());
            return;
        }

        if (player.isAfterLogin()) { // 登录事件
            player.setAfterLogin(false);
            EventUtil.fireEvent(EventType.PLAYER_LOGIN, player);
        }

        LOGGER.error("{}进入地图地图：new->{}", player.getName(), player.getMapId());

        GameMap map = getMap(player);
        if (map == null) {
            return;
        }

        /*if(!map.isReady()){
        	TipManager.getInstance().error(player.getId(), "不能进入该地图");
        	return;
        }*/

        // 进入地图
        enterMap(player, player.getMapId(), player.getLine(), player.getX(), player.getY());

        // 进入地图事件
        EventUtil.fireEvent(EventType.ENTER_MAP, player);
    }

    /**
     * 玩家进入地图
     *
     * @param player
     * @param mapId
     * @param line
     * @param x
     * @param y
     */
    public void enterMap(Player player, int mapId, int line, int x, int y) {
        GameMap map = getMap(mapId, line);
        if (map == null) {
            return;
        }

        MapPoint point = map.getPoint(x, y);
        if (point == null || !point.canStand(player)) {
            point = randomAvailablePoint(map, x, y, 5);
        }

        map.enterPlayer(player, point);
        player.setLoginMap(true);

        // 兼容错误
        if (player.getPoint() == null) {
            player.setPoint(MapManager.getInstance().randomAvailablePoint(map));
        }

//        MessageGroup group = new MessageGroup();
//        ResPlayerEnterMapMessage res = new ResPlayerEnterMapMessage();
//        res.setLid(player.getId());
//        res.setX(player.getPoint().getX());
//        res.setY(player.getPoint().getY());
//        res.setDir(player.getDir());
//        res.setHp(player.getHp());
//        res.setMp(player.getMp());
//        group.addMessage(res);

        // 发送周围的视野
        List<IMapObject> roundList =
                map.getAoi().getObjectListByRange(player.getPoint(), TowerAOI.RANGE_DEFAULT);

        LOGGER.info("{}进入地图，所在位置：{}", player.getName(), player.getPoint());
//        Message msg =
//                AOIEventListenerImpl.buildUpdateViewMessage(
//                        roundList, Collections.<IMapObject>emptyList(), player);
//
//        group.addMessage(msg);
//
//        MessageUtil.sendMsg(group, player.getId());
    }

    /**
     * 改变地图
     *
     * @param player
     * @param mapId
     * @param line
     * @param x
     * @param y
     */
    public boolean changeMap(Player player, int mapId, int line, int x, int y) {

        LOGGER.error("{}切换地图：old->{},new->{}", player.getName(), player.getMapId(), mapId);

        if (player.getMapId() == mapId && player.getLine() == line) {
            if (player.getPoint() == null) {
                return false;
            }
            if (x == player.getPoint().getX() && y == player.getPoint().getY()) {
                return false;
            } else {
                changePos(player, x, y, true);
                return false;
            }
        }

        GameMap newMap = getMap(mapId, line);
        if (newMap == null) {
            return false;
        }

        GameMap oldMap = getMap(player);
        if (oldMap == null) {
            return false;
        }

        if (!oldMap.isInstance()) {
            player.setLastMapId(oldMap.getId());
            player.setLastX(player.getPoint().x);
            player.setLastY(player.getPoint().y);
        }

        EventUtil.fireEvent(EventType.QUIT_MAP, player);

        oldMap.removePlayer(player);

        // 1.设置新场景
        player.setMapId(mapId);
        player.setLine(line);

        // 设置进入的x和y
        player.setX(x);
        player.setY(y);

        // 设置标志
        player.setLoginMap(false);

        player.getRole().setX(x);
        player.getRole().setY(y);
        player.getRole().setMapId(mapId);
        player.getRole().setLine(line);

        //		ResPlayerChangeMapMessage res = new ResPlayerChangeMapMessage();
        //		res.setMid(newMap.getCfgId());
        //		res.setLine(line);
        //		res.setX(x);
        //		res.setY(y);
        //
        //		MessageUtil.sendMsg(res, player.getId());
        DataCenter.updateData(player.getRole());
        return true;
    }

    public void changePos(Performer performer, int x, int y) {
        changePos(performer, x, y, false);
    }

    /**
     * 改变位置
     *
     * @param performer
     * @param x
     * @param y
     * @param send
     */
    public void changePos(Performer performer, int x, int y, boolean send) {
        GameMap map = getMap(performer.getMapId(), performer.getLine());

        MapPoint oldPoint = performer.getPoint();

        MapPoint newPoint = map.getPoint(x, y);
        if (newPoint == null) {
            return;
        }

        if (send) {
//            ResChangePosMessage res = new ResChangePosMessage();
//            res.setX(x);
//            res.setY(y);
//            res.setLid(performer.getId());
//
//            MessageUtil.sendMsg(res, performer.getId());
        }
        map.stand(performer, x, y);

        map.updateView(performer, oldPoint);
    }

    /**
     * 随机一个可用的点（没有道具和NPC）
     *
     * @param map
     * @param x
     * @param y
     * @param range
     * @return
     */
    public MapPoint randomAvailablePoint(GameMap map, int x, int y, int range) {
        int[] offset = GeomUtil.getPointRoundOffset(range);

        List<MapPoint> gridList = new ArrayList<MapPoint>();
        for (int i = 0; i < offset.length; i += 2) {
            int offx = x + offset[i];
            int offy = y + offset[i + 1];

            MapPoint point = map.getPoint(offx, offy);
            if (point == null) {
                continue;
            }

            if (point.canStand(null) && !point.isHasTransmitEvent()) {
                gridList.add(point);
            }
        }

        if (!gridList.isEmpty()) {
            return gridList.get(ThreadLocalRandom.current().nextInt(gridList.size()));
        }
        return null;
    }

    public MapPoint randomAvailablePoint(GameMap map) {

        List<MapPoint> walkList = map.getTopography().getWalkList();
        if (walkList.isEmpty()) {
            return null;
        }
        return walkList.get(ThreadLocalRandom.current().nextInt(walkList.size()));
    }

    public MapPoint randomAvailablePoint(GameMap map, int x, int y) {

        int range = 1;
        MapPoint stand = MapManager.getInstance().randomAvailablePoint(map, x, y, range);
        while (stand == null && range < 12) {
            stand = MapManager.getInstance().randomAvailablePoint(map, x, y, range);
            range++;
        }
        return stand;
    }

    public MapPoint randomDropPoint(GameMap map, int x, int y) {

        int range = 1;
        while (range < 10) {
            int[] offset = GeomUtil.getPointRoundOffset(range);

            List<MapPoint> gridList = new ArrayList<MapPoint>();
            for (int i = 0; i < offset.length; i += 2) {
                int offx = x + offset[i];
                int offy = y + offset[i + 1];

                MapPoint point = map.getPoint(offx, offy);
                if (point == null) {
                    continue;
                }
                if (point.isBlock()) {
                    continue;
                }

                if (point.hasNotNpcAndItem() && !point.isHasTransmitEvent()) {
                    gridList.add(point);
                }
            }

            if (!gridList.isEmpty()) {
                return gridList.get(ThreadLocalRandom.current().nextInt(gridList.size()));
            }
            range++;
        }
        return null;
    }

    public void quitMap(GameMap map, Player player) {
        map.removePlayer(player);
    }

    public void deliver(Player player, int deliverId) {}

    /**
     * 只有基本判断条件判断的传送,外界调用的时候,必须自己做强验证
     *
     * @param player
     * @param mapId
     * @param line
     * @param x
     * @param y
     * @return
     */
    public boolean transmit(Player player, int mapId, int line, int x, int y) {

        if (player == null) {
            return false;
        }

        GameMap oldMap = MapManager.getInstance().getMap(player);

        GameMap newMap = MapManager.getInstance().getMap(mapId, line);

        if (oldMap == null || newMap == null) {
            return false;
        }

        // 传送条件判断（例如押镖、摆摊、buffer等等

        MapPoint point = newMap.getPoint(x, y);
        if (point == null) {
//            TipManager.getInstance().error(player.getId(), "目标不可达");
            return false;
        }

        MapPoint stand = randomAvailablePoint(newMap, x, y);
        if (stand == null) {
//            TipManager.getInstance().error(player.getId(), "目标不可达");
            return false;
        }

        if (player.getMapId() == mapId && player.getLine() == line) {
            changePos(player, x, y, true);
            return true;
        } else {
            changeMap(player, mapId, line, x, y);
            return true;
        }
    }
}
