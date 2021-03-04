package info.xiaomo.server.rpg.map;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.persist.mysql.persist.PersistAble;
import info.xiaomo.gengine.utils.Args;
import info.xiaomo.server.rpg.db.DataType;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

@Data
public class Role implements PersistAble {

    @Exclude private boolean dirty = false;

    /** 所有系统的属性集合 */
    @Exclude
    private Map<AttributeType, Args.Two<Attribute, Attribute>> attributes = new HashMap<>();

    /** id */
    @Tag(1)
    private long id;

    /** name */
    @Tag(2)
    private String name;

    /** 性别 */
    @Tag(3)
    private int sex;

    /** 职业 */
    @Tag(4)
    private int career;

    /** 等级 */
    @Tag(5)
    private int level;

    /** x */
    @Tag(6)
    private int x;

    /** y */
    @Tag(7)
    private int y;

    /** mapId */
    @Tag(8)
    private int mapId;

    /** line */
    @Tag(9)
    private int line;

    /** 上个x */
    @Tag(10)
    private int lastX;

    /** 上个y */
    @Tag(11)
    private int lastY;

    /** 上个地图id */
    @Tag(12)
    private int lastMapId;

    /** 方向 */
    @Tag(13)
    private int dir;

    /** 血量 */
    @Tag(14)
    private int hp;

    /** cd相关 */
    @Tag(15)
    private Map<Long, CD> cdMap = new HashMap<>();

    /** buff */
    @Tag(16)
    private Map<Integer, Buff> bufferMap = new HashMap<>();

    /** 经验值 */
    @Tag(17)
    private long exp;

    /** 装备信息 */
    @Tag(18)
    private Map<Integer, Item> equipMap = new HashMap<>();

    /** 魔法值 */
    @Tag(19)
    private int mp;

    /** 角色属性 */
    @Tag(20)
    private Attribute finalAttribute;

    /** VIP等级 */
    @Tag(31)
    private int vipLevel;

    /** 玩家离线时间 */
    @Tag(22)
    private long offlineTime;

    /** 角色设置 */
    @Tag(23)
    private Map<Integer, Map<Integer, Integer>> roleSettings = new HashMap<>();

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public int dataType() {
        return DataType.ROLE;
    }

    @Override
    public int getServerId() {
        return 0;
    }
}
