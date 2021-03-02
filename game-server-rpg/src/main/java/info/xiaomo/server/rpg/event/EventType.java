package info.xiaomo.server.rpg.event;

import info.xiaomo.gengine.event.BasicEventType;

public interface EventType extends BasicEventType {

    Integer ENTER_MAP = 1;

    Integer QUIT_MAP = 2;

    Integer PLAYER_LOGIN = 3;

    Integer MONSTER_DIE = 4;

    Integer PLAYER_DIE = 5;
}
