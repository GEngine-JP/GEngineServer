package info.xiaomo.server.rpg.system.gm;


import java.util.HashMap;
import java.util.Map;

import info.xiaomo.server.rpg.system.gm.command.Gm;
import info.xiaomo.server.rpg.system.gm.command.impl.CloseServerGm;

/**
 * Copyright(©) 2017 by xiaomo.
 */
public enum GMCommand {

    /**
     * 关服
     */
    关服(1);

    private final int gmLevel;

    GMCommand(int gmLevel) {
        this.gmLevel = gmLevel;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    private static final Map<String, GMCommand> COMMAND_MAP = new HashMap<>(10);

    private static final Map<GMCommand, Gm> G_M_MAP = new HashMap<>(10);

    static {
        for (GMCommand command : GMCommand.values()) {
            COMMAND_MAP.put(command.name(), command);
        }
    }

    static {
        G_M_MAP.put(GMCommand.关服, new CloseServerGm());
    }

    public static GMCommand getCommand(String command) {
        return COMMAND_MAP.get(command);
    }

    public static Gm getGm(GMCommand command) {
        return G_M_MAP.get(command);
    }

}
