package info.xiaomo.server.system.gm;

import info.xiaomo.server.system.gm.command.Gm;
import info.xiaomo.server.system.gm.command.impl.CloseServerGm;

import java.util.HashMap;
import java.util.Map;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 15:35
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public enum GMCommand {

    /**
     * 关服
     */
    关服(1);

    private int gmLevel;

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
