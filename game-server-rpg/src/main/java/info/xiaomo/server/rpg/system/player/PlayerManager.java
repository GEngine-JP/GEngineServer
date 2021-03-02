package info.xiaomo.server.rpg.system.player;

import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.map.Player;

public class PlayerManager {

    private static PlayerManager INSTANCE;

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public void playerDie(AbstractGameMap map, Player performer) {}
}
