package info.xiaomo.server.rpg.map;

import info.xiaomo.gengine.ai.fsm.FSMMachine;
import info.xiaomo.gengine.map.constant.MapConst;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.obj.MapObjectType;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.gengine.map.obj.Pet;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterConst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends Performer {
    /** 只在上线/切换地图的时候才会变化(实时坐标点取point的x) */
    private int x;

    /** 只在上线/切换地图的时候才会变化(实时坐标点取point的y) */
    private int y;

    private Role role;

    /** 刚刚登陆 */
    private boolean afterLogin;

    /** 是否已下线 */
    private boolean offline;

    /** 队伍id */
    private int teamId;

    /** 是否原地复活 */
    private boolean reliveHere;

    /** 原地复活剩余时间 */
    private int reliveHereTime;

    /** 回城复活时间 */
    private int reliveHomeTime;

    private FSMMachine<Player> machine;

    private boolean loginMap;

    private long unionId;

    @Override
    public FSMMachine<Player> getMachine() {
        return machine;
    }

    public void setMachine(FSMMachine<Player> machine) {
        this.machine = machine;
    }

    public Player(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public long getId() {
        return role.getId();
    }

    @Override
    public void setId(long id) {}

    @Override
    public int getConfigId() {
        return 0;
    }

    @Override
    public void setConfigId(int configId) {}

    @Override
    public int getType() {
        return MapObjectType.Player;
    }

    @Override
    public int getLevel() {
        return role.getLevel();
    }

    @Override
    public void setLevel(int level) {
        if (level == 0) {
            System.out.println(
                    "--------------------------等级设置有问题，请检查！！！！！！！！！！！！！----------------");
        }
        role.setLevel(level);
    }

    @Override
    public boolean penetrate(IMapObject obj) {
        if (obj.getType() == MapObjectType.Player) {
            GameMap map = MapManager.getInstance().getMap(this);
            Player player = (Player) obj;
            if (player.isDead()) {
                return true;
            }
            return map.isCanCross();
        } else if (obj.getType() == MapObjectType.Monster) {
            Monster monster = (Monster) obj;
            if (monster.isDead()) {
                return true;
            }
            return monster.getMonsterType() != MonsterConst.MonsterType.BOSS;
        } else {
            return obj.getType() != MapObjectType.NPC;
        }
    }

    @Override
    public boolean overlying(IMapObject obj) {
        return false;
    }

    /**
     * 是否是敌人
     *
     * @param obj obj
     * @return boolean
     */
    @Override
    public boolean isEnemy(IMapObject obj) {

        if (this == obj) {
            return false;
        } else if (obj.getType() == MapObjectType.Monster) {
            return true;
        } else if (obj.getType() == MapObjectType.Player) {
            GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
            if (map == null) {
                return false;
            }
            return MapConst.MapId.DEAD_FIGHT.contains(map.getCfgId());

            // http://192.168.1.16/index.php?m=story&f=view&storyID=20501 玩家不能攻击玩家
        }
        return false;
    }

    @Override
    public boolean isFriend(IMapObject obj) {
        if (this.isEnemy(obj)) {
            return false;
        }
        if (this == obj) {
            return true;
        } else if (obj.getType() == MapObjectType.Player) {
            return true;
        } else if (obj.getType() == MapObjectType.Pet) {
            Pet pet = (Pet) obj;
            return isFriend(pet.getMaster());
        } else {
            return false;
        }
    }

    public boolean isAfterLogin() {
        return afterLogin;
    }

    public void setAfterLogin(boolean afterLogin) {
        this.afterLogin = afterLogin;
    }

    @Override
    public int getDir() {
        return role.getDir();
    }

    @Override
    public void setDir(int dir) {
        role.setDir(dir);
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getLastX() {
        return role.getLastX();
    }

    public void setLastX(int lastX) {
        this.role.setLastX(lastX);
    }

    public int getLastY() {
        return role.getLastY();
    }

    public void setLastY(int lastY) {
        this.role.setLastY(lastY);
    }

    public int getLastMapId() {
        return role.getLastMapId();
    }

    public void setLastMapId(int lastMapId) {
        role.setLastMapId(lastMapId);
    }

}
