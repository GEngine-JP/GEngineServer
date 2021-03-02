package info.xiaomo.server.rpg.fsm.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.ai.util.WalkUtil;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.IMove;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.gengine.utils.RandomUtil;
import info.xiaomo.server.rpg.system.move.MoveManager;
import info.xiaomo.server.rpg.system.skill.MonsterSkill;
import info.xiaomo.server.rpg.system.skill.SkillConst;
import info.xiaomo.server.rpg.map.GameMap;
import info.xiaomo.server.rpg.map.MapManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonsterAIUtil {

    /**
     * 获取一个最近的可以攻击的对象
     *
     * @param monster monster
     * @return
     */
    public static Performer findNearestFightAblePerformer(Monster monster) {

        GameMap map = MapManager.getInstance().getMap(monster.getMapId(), monster.getLine());

        if (map == null) {
            return null;
        }

        // 最小一秒查找周围目标间隔
        AIData aiData = monster.getMachine().getAiData();
        // http://192.168.5.65:8080/index.php?m=story&f=view&storyID=21817
        if (aiData.getFindNearestFightAbleTime() > 0) {
            return null;
        }

        // 重新赋值
        int heart = Math.max(monster.getHeart(), 1000);
        aiData.setFindNearestFightAbleTime(heart);

        int nearestDis = Integer.MAX_VALUE;
        IMapObject nearestObj = null;

        MapPoint p = monster.getPoint();
        // int[] offset = GeomUtil.getPointRoundOffset(9);

        List<IMapObject> ret = getMapList(monster, map, p);
        for (IMapObject obj : ret) {

            if (!(obj instanceof Performer)) {
                continue;
            }

            Performer performer = (Performer) obj;

            MapPoint targetP = performer.getPoint();

            if (performer.isDead()) {
                continue;
            }

            int dis = GeomUtil.distance(p, targetP);
            if (dis > monster.getToAttackArea()) {
                continue;
            }

            if (!monster.isEnemy(obj)) {
                continue;
            }
            if (nearestDis > dis) {
                nearestDis = dis;
                nearestObj = obj;
            }
        }

        return (Performer) nearestObj;
    }

    /**
     * 回家
     *
     * @param monster
     * @param disToBirth
     * @param delta
     */
    public static void backHome(Monster monster, int disToBirth, int delta) {
        AIData aiData = monster.getMachine().getAiData();
        if (monster.getMoveSpeed() > 0) {
            int area = monster.getToAttackArea() / 2;
            if (area > 40) {
                area = 40;
            }
            if (disToBirth > area) { // 超过警戒范围的一半，就往出生点走
                MonsterAIUtil.homeWalk(monster);
            } else if (disToBirth <= area) { // 没超过警戒范围一半，就随机走
                if (aiData.getRandomMoveTime() <= 0) {
                    randomWalk(monster);
                    aiData.setRandomMoveTime(ThreadLocalRandom.current().nextInt(5000) + 5000);
                }
            }
        }
    }

    /**
     * 回到怪的出生区域
     *
     * @param monster
     */
    public static void homeWalk(Monster monster) {

        MapPoint tpoint = monster.getBirthPoint();
        GameMap map = MapManager.getInstance().getMap(monster);
        // 获取下一个可以行走的点
        MapPoint point =
                WalkUtil.getPathNext(map, monster, tpoint, monster.getMoveStep(), WalkUtil.MOVE);

        if (point != null && point != tpoint && point.canStand(monster)) {
            // 怪物移动到改点
            MoveManager.getInstance().performerMove(map, monster, point);
        } else {
            WalkUtil.moveByDir(map, monster, tpoint, MoveManager.getInstance());
        }
    }

    /**
     * 随机走动
     *
     * @param monster
     */
    public static void randomWalk(Monster monster) {
        MapPoint mPoint = monster.getPoint();
        GameMap map = MapManager.getInstance().getMap(monster);
        int dirIndex = 0;

        if (monster.getLevel() > 40) { // 40级以上的怪沿着8个方向走
            dirIndex = ThreadLocalRandom.current().nextInt(8);
        } else { // 40级（含40）以下的怪沿着4方向走
            dirIndex = ThreadLocalRandom.current().nextInt(4) * 2 + 1;
        }

        Dir dir = Dir.getByIndex(dirIndex);
        MapPoint nextPoint = GeomUtil.nextDirPoint(mPoint, dir);
        if (nextPoint != null && nextPoint.canStand(monster)) {
            if (monster.getMoveStep() == 2) {
                MapPoint tempPoint = GeomUtil.nextDirPoint(nextPoint, dir);
                if (tempPoint != null && tempPoint.canStand(monster)) {
                    nextPoint = tempPoint;
                }
            }

            int disToHome = GeomUtil.distance(nextPoint, monster.getBirthPoint());
            if (disToHome > monster.getToAttackArea()) {
                return;
            }

            MoveManager.getInstance().performerMove(map, monster, nextPoint);
        }
    }

    /**
     * 寻找最高仇恨的目标
     *
     * @param map
     * @param monster
     * @return
     */
    public static Performer findHighestThreatPerformer(AbstractGameMap map, AbstractMonster monster) {
        int maxValue = -1;
        Performer ret = null;
        Map<Long, Integer> threatMap = monster.getThreatMap();

        List<Long> removeList = new ArrayList<>();

        for (long key : threatMap.keySet()) {

            int value = threatMap.get(key);
            Performer performer = (Performer) map.getObject(key);
            if (performer == null) {
                continue;
            }
            if (performer.isDead()) {
                // 移除仇恨
                removeList.add(key);
                continue;
            }
            if (value > maxValue) {
                ret = performer;
                maxValue = value;
            }
        }
        for (Long removeId : removeList) {
            threatMap.remove(removeId);
        }
        if (ret == null) {
            monster.setFightTarget(0);
        }
        return ret;
    }

    /**
     * 更新怪物仇恨（随着时间推移更新）
     *
     * @param monster
     * @param dt
     */
    public static void updateThreat(Monster monster, int dt) {
        AIData ai = monster.getMachine().getAiData();
        int clearTime = ai.getClearThreatTime();
        if (clearTime % 9000 < dt) { // [9000, 9000+dt-1] 以及 [0 ,dt-1] 执行 假设
            // 心跳为60毫秒准时 那么 9000的时候执行一次
            Map<Long, Integer> threatMap = monster.getThreatMap();
            threatMap.replaceAll((k, v) -> 1);

        } else if (clearTime % 4500 < dt) { // [4500, 4500+dt-1] 假设心跳为60毫秒准时，那么
            // 4500 的时候执行一次
            Map<Long, Integer> threatMap = monster.getThreatMap();
            boolean empty = true;
            for (long key : threatMap.keySet()) {
                int value = threatMap.get(key);
                if (value > 1) {
                    empty = false;
                } else {
                    threatMap.remove(key);
                    if (monster.getOwner() > 0 && key == monster.getOwner()) {
                        monster.setOwner(0);
                    }
                }
            }
            if (empty) {
                // 清除空仇恨列表
                // System.out.println("清空仇恨:");
                threatMap.clear();
                monster.setOwner(0);
                monster.setFightTarget(0);
            }
        }
        if (clearTime < dt) { // [0 ,dt-1]执行 假设心跳为60毫秒准时，那么0秒的时候执行一次
            clearTime = clearTime + 9000;
            ai.setClearThreatTime(clearTime);
        }
    }

    /**
     * 回血
     *
     * @param monster
     * @param timeInterval
     */
    public static void recoverHp(Monster monster, int timeInterval) {

        AIData ai = monster.getMachine().getAiData();
        int recoverTime = ai.getRecoverHpAndMapTime();
        if (recoverTime <= 0) {
            ai.setRecoverHpAndMapTime(timeInterval);
            int diff = monster.getFinalAttribute().getMaxHp() - monster.getHp();
            if (diff <= 0) {
                return;
            }

            int recoverHp = monster.getFinalAttribute().getFightRec();
            if (recoverHp <= 0) {
                return;
            }

            if (recoverHp >= diff) {
                recoverHp = diff;
            }
            monster.setHp(monster.getHp() + recoverHp);

            // 同步消息
            //            ResHpChangeMessage msg = new ResHpChangeMessage();
            //            msg.setLid(monster.getId());
            //            msg.setHp(monster.getHp());
            //            MessageUtil.sendRoundMessage(msg, monster);
        }
    }

    /**
     * 追击玩家
     *
     * @param map
     * @param monster
     * @param target
     */
    public static final void chaseWalk(GameMap map, Monster monster, Performer target) {
        MapPoint tPoint = target.getPoint();

        MapPoint point =
                WalkUtil.getPathNext(
                        map, monster, tPoint, monster.getMoveStep(), WalkUtil.MOVE_TO_ATTACK);
        if (point != null && point.canStand(monster) && tPoint != point) {
            MoveManager.getInstance().performerMove(map, monster, point);
        } else {
            WalkUtil.moveByDir(map, monster, tPoint, MoveManager.getInstance());
        }
    }

    /**
     * 传送
     *
     * @param map
     * @param monster
     * @param target
     */
    public static void deliver(GameMap map, Monster monster, Performer target) {
        MapPoint npoint = monster.getPoint();
        MapPoint tpoint = target.getPoint();
        if (npoint != null && tpoint != null) {
            int dir = GeomUtil.distance(npoint, tpoint);
            if (dir > 20) {
                return;
            }
        }
        if (!tpoint.canStand(monster) && !tpoint.isSafe()) {
            int range = 2;
            MapPoint temp =
                    MapManager.getInstance().randomAvailablePoint(map, tpoint.x, tpoint.y, range);
            while (temp != null && !temp.canStand(monster) && !tpoint.isSafe() && range < 4) {
                range += 1;
                temp =
                        MapManager.getInstance()
                                .randomAvailablePoint(map, tpoint.x, tpoint.y, range);
            }
            if (temp != null) {
                tpoint = temp;
            } else {
                tpoint = null;
            }
        }
        if (tpoint == null) {
            return;
        }

        MapManager.getInstance().changePos(monster, tpoint.x, tpoint.y);
    }

    /**
     * 跟着走
     *
     * @param map
     * @param monster
     * @param target
     * @param step
     * @param action
     * @return
     */
    public static boolean followWalk(
            IMove move, GameMap map, Monster monster, MapPoint target, int step, int action) {
        if (monster == null) {
            return false;
        }
        // 寻找下一个移动位置
        MapPoint point = WalkUtil.getPathNext(map, monster, target, step, action);

        if (point != null && point.canStand(monster)) {
            move.performerMove(map, monster, point);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 找技能
     *
     * @param monster
     * @param target
     * @return
     */
    public static int findSkill(Monster monster, Performer target) {
        // 和目标的距离
        int tdis = GeomUtil.distance(monster.getPoint(), target.getPoint());
        // 当前血量百分比
        int hpPercent =
                (int) (monster.getHp() / (float) monster.getFinalAttribute().getMaxHp() * 100);

        int skillId = 0;

        long curTime = System.currentTimeMillis();
        List<MonsterSkill> skills = new ArrayList<>();
        int currentPriority = -1;
        for (MonsterSkill skill : monster.getSkillArray()) {
            if (hpPercent <= skill.getHpPercentMax()
                    && hpPercent >= skill.getHpPercentMin()
                    && tdis <= skill.getAttackDistance()) {
                if (currentPriority != -1) {
                    if (currentPriority > skill.getPriority()) {
                        skills.clear();
                        skills.add(skill);
                        currentPriority = skill.getPriority();
                    } else if (currentPriority < skill.getPriority()) {
                    } else {
                        skills.add(skill);
                    }
                } else {
                    skills.add(skill);
                    currentPriority = skill.getPriority();
                }
            }
        }

        if (!skills.isEmpty()) {
            // 获取一个技能执行攻击
            MonsterSkill skill;
            final int size = skills.size();
            if (size == 1) {
                skill = skills.get(0);
            } else {
                // 同一个优先级有多个，那么就随机一个技能
                int index = ThreadLocalRandom.current().nextInt(size);
                skill = skills.get(index);
            }
            skillId = skill.getSkillId();
        }

        if (skillId == 0) { // 默认用普通攻击
            if (tdis <= 1) {
                skillId = SkillConst.Attack;
            }
        }
        return skillId;
    }

    /**
     * 寻找范围内的随机一个对象
     *
     * @param monster monster
     * @return Performer
     * @return chooseInterValTime 重新随机目标间隔时间
     */
    public static Performer findRandomTarget(Monster monster, int chooseInterValTime) {
        GameMap map = MapManager.getInstance().getMap(monster.getMapId(), monster.getLine());

        if (map == null) {
            return null;
        }

        // 最小一秒查找周围目标间隔
        AIData aiData = monster.getMachine().getAiData();
        if (aiData.getFindNearestFightAbleTime() > 0) {
            return (Performer) map.getObject(monster.getFightTarget());
        }

        aiData.setFindNearestFightAbleTime(chooseInterValTime * 1000);

        int nearestDis = Integer.MAX_VALUE;
        List<Performer> list = new ArrayList<>();

        MapPoint p = monster.getPoint();

        List<IMapObject> ret = getMapList(monster, map, p);
        for (IMapObject obj : ret) {

            if (!(obj instanceof Performer)) {
                continue;
            }

            Performer performer = (Performer) obj;

            MapPoint targetP = performer.getPoint();
            if (performer.isDead()) {
                continue;
            }

            int dis = GeomUtil.distance(p, targetP);
            if (dis > monster.getToAttackArea()) {
                log.debug("怪物:{}追击范围{} 超过上限{}", monster.getName(), dis, monster.getToAttackArea());
                continue;
            }

            if (!monster.isEnemy(obj)) {

                continue;
            }
            if (nearestDis > dis) {
                list.add(performer);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        int index = RandomUtil.random(0, list.size() - 1);
        return list.get(index);
    }

    /**
     * 根据指定目标
     *
     * @param monster monster
     * @return Performer
     */
    public static Performer findPointedTarget(Monster monster, int targetMid) {
        GameMap map = MapManager.getInstance().getMap(monster.getMapId(), monster.getLine());
        if (map == null) {
            return null;
        }
        Map<Long, AbstractMonster> monsterMap = map.getMonsterMap();
        for (AbstractMonster mMonster : monsterMap.values()) {
            if (mMonster.getConfigId() == targetMid) {
                long fightTarget = mMonster.getFightTarget();
                if (fightTarget == 0) {
                    return null;
                }
                return (Performer) map.getObject(fightTarget);
            }
        }
        return null;
    }

    /**
     * 获取对象列表
     *
     * @param monster monster
     * @param map map
     * @param p p
     * @return List<IMapObject>
     */
    private static List<IMapObject> getMapList(Monster monster, GameMap map, MapPoint p) {
        List<IMapObject> list;
        if (monster.getMonsterType() == MonsterConst.MonsterType.BOSS) {
            if (monster.getToAttackArea() > 15) { // 大于最大攻击范围 则用3个大格子
                list = map.getAoi().getObjectListByRange(p, 3);
            } else {
                list = map.getAoi().getObjectListByRange(p, 2);
            }
        } else {
            list = map.getAoi().getObjectListByRange(p, 1);
        }
        return list;
    }
}
