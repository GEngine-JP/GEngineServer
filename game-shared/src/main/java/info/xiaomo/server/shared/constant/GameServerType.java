package info.xiaomo.server.shared.constant;

import info.xiaomo.gengine.network.server.ServerType;

public enum GameServerType {
    GAME_BYDR(101);


    private final int type;

    GameServerType(int type) {
        this.type = type;
    }

    public static ServerType valueOf(int type) {
        for (ServerType t : ServerType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return ServerType.NONE;
    }

    public int getType() {
        return type;
    }
}