package info.xiaomo.server.rpg.config.tables;

import info.xiaomo.gengine.config.IConfig;
import lombok.Data;

/** 地图配置 */
@Data
public class MapConfig implements IConfig {

    /** 地图id */
    private int id;
    /** 地图名字 */
    private String name;

    /** 分线 */
    private int line;

    /** 地图宽度 */
    private int width;

    /** 地图高度 */
    private int height;

    /** 是否可以无条件穿人 */
    private int cancross;

    /** 屏蔽 1 */
    private int forbidden;

    /** 是否是安全地图 */
    private int safe;

    /** 城镇复活的地图id */
    private int homeMapId;

    /** 城镇复活的x */
    private int homeX;

    /** 城镇复活的y */
    private int homeY;

    /** 玩家复活需要的时间 */
    private int reliveHomeTime;

    /** 是否可以传送 1 不允许传送 */
    private int canFly;

    /** 原地复活血量 */
    private int recoverPercent;

    private int[] reliveType;

    /** 原地附后需要的道具 id#count */
    private int[] reliveHereItem;
}
