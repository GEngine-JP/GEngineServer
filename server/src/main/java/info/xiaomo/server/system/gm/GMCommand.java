package info.xiaomo.server.system.gm;

import info.xiaomo.server.system.gm.command.GM;
import info.xiaomo.server.system.gm.command.impl.CloseServerGM;

import java.util.HashMap;
import java.util.Map;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 15:35
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public enum GMCommand {


    关服(1);

    private int gmLevel;

    GMCommand(int gmLevel) {
        this.gmLevel = gmLevel;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    private static final Map<String, GMCommand> commandMap = new HashMap<>();

    private static final Map<GMCommand, GM> GMMap = new HashMap<>();

    static {
        for (GMCommand command : GMCommand.values()) {
            commandMap.put(command.name(), command);
        }
    }

    static {
        GMMap.put(GMCommand.关服, new CloseServerGM());
    }

    public static GMCommand getCommand(String command) {
        return commandMap.get(command);
    }

    public static GM getGm(GMCommand command) {
        return GMMap.get(command);
    }

}
