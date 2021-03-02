package info.xiaomo.server.rpg.map;

import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.Topography;
import info.xiaomo.gengine.map.aoi.AOIEventListener;
import info.xiaomo.gengine.map.obj.*;
import info.xiaomo.server.rpg.event.EventType;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameMap extends AbstractGameMap {

    /** 是否是副本 */
    private boolean instance;

    public GameMap(Topography topography, AOIEventListener listener) {
        super(topography, listener);
    }

    @Override
    public void add(IMapObject obj) {
        objectMap.put(obj.getId(), obj);
        if (obj.getType() == MapObjectType.Player) {
            playerMap.put(obj.getId(), (Player) obj);
        } else if (obj.getType() == MapObjectType.Monster) {
            monsterMap.put(obj.getId(), (Monster) obj);
        } else if (obj.getType() == MapObjectType.BUFFER) {
            //		    bufferMap.put(obj.getId(), (GroundBuffer) obj);
        } else if (obj.getType() == MapObjectType.NPC) {
            npcMap.put(obj.getId(), (NPC) obj);
        } else if (obj.getType() == MapObjectType.EVENT) {
            eventMap.put(obj.getId(), (GroundEvent) obj);
        } else if (obj.getType() == MapObjectType.Pet) {
            petMap.put(obj.getId(), (Pet) obj);
        }
    }

    @Override
    public void remove(IMapObject obj) {
        objectMap.remove(obj.getId());
        if (obj.getType() == MapObjectType.Player) {
            playerMap.remove(obj.getId());
        } else if (obj.getType() == MapObjectType.Monster) {
            monsterMap.remove(obj.getId());
        } else if (obj.getType() == MapObjectType.BUFFER) {
            //		    bufferMap.remove(obj.getId());
        } else if (obj.getType() == MapObjectType.NPC) {
            npcMap.remove(obj.getId());
        } else if (obj.getType() == MapObjectType.EVENT) {
            eventMap.remove(obj.getId());
        } else if (obj.getType() == MapObjectType.Pet) {
            petMap.remove(obj.getId());
        }
    }

    @Override
    public void enterPlayer(Performer player, MapPoint point) {
        player.setMapId(this.id);
        player.setLine(this.line);
        if (point == null) {
            log.info("玩家{}->{} 进入地图找不到点", player.getId(), player.getName());
        }
        stand(player, point);
        objectMap.put(player.getId(), player);
        playerMap.put(player.getId(), player);
        aoi.addObject(player, player.getPoint());
        aoi.addWatcher(player, player.getPoint());
    }

    @Override
    public void removePlayer(Performer player) {
        EventUtil.fireEvent(EventType.QUIT_MAP, player);

        aoi.removeWatcher(player, player.getPoint());
        aoi.removeObject(player, player.getPoint());
        objectMap.remove(player.getId());
        playerMap.remove(player.getId());
        stand(player, null);
    }

    public boolean isInstance() {
        return instance;
    }

    public void setInstance(boolean instance) {
        this.instance = instance;
    }
}
