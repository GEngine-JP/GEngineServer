package info.xiaomo.server.rpg.fsm.monster.ai.impl;

import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterAIUtil;
import info.xiaomo.server.rpg.fsm.monster.MonsterConst;
import info.xiaomo.server.rpg.fsm.monster.ai.AbstractMonsterAI;
import info.xiaomo.server.rpg.system.skill.SkillConst;
import info.xiaomo.server.rpg.map.GameMap;

/**
 * 主动怪物ai
 *
 * @author zhangli 2017年6月6日 下午9:56:17
 */
public class ActiveAI extends AbstractMonsterAI {

    @Override
    public boolean activeUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {

        boolean ret = super.activeUpdate(map, monster, dt);
        if (!ret) {
            return false;
        }
        MapPoint birth = monster.getBirthPoint();

        MapPoint cur = monster.getPoint();

        int disToBirth = GeomUtil.distance(birth, cur);

        Performer nearestPerformer = null;
        if (disToBirth <= monster.getToAttackArea()) {
            // 位于巡逻路径之内，更新 一下自己最近敌人
            nearestPerformer = MonsterAIUtil.findNearestFightAblePerformer((Monster) monster);
        }
        if (nearestPerformer != null) {
            //            MonsterManager.getInstance().addThreat(monster, nearestPerformer, true);
        } else {
            // 巡逻或者回家
            MonsterAIUtil.backHome((Monster) monster, disToBirth, dt);

            int moveInterval = Math.max(monster.getMoveInterval(), 400);
            AIData aiData = monster.getMachine().getAiData();
            aiData.setSleepTime(moveInterval);
        }
        return true;
    }

    @Override
    public boolean battleUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        boolean ret = super.battleUpdate(map, monster, dt);
        if (!ret) {
            return false;
        }

        Performer target = (Performer) map.getObject(monster.getFightTarget());

        int sleepTime = 0;
        int skillId = findSkill(monster, target);
        if (skillId == 0) { // 技能id为0，说明没找到合适的技能（就算所有技能都CD中，都还有普通技能，这了实际上说明距离目标稍微远了 一点）
            if (monster.getMoveSpeed() > 0) {
                MapPoint oldPoint = monster.getPoint();
                MonsterAIUtil.chaseWalk((GameMap) map, (Monster) monster, target);
                if (monster.getMonsterType() == MonsterConst.MonsterType.BOSS) {
                    if (monster.getPoint() == oldPoint) { // 没有移动过去，就直接传送过去
                        MonsterAIUtil.deliver((GameMap) map, (Monster) monster, target);
                    }
                }

                sleepTime = Math.max(monster.getMoveInterval(), 400);
            }
        } else {
            Monster m = (Monster) monster;
            monsterCastSkill(monster, target, skillId);
            sleepTime = m.getFinalAttribute().getAttackSpeed();
        }
        monster.getMachine().getAiData().setSleepTime(sleepTime);
        return true;
    }

    /**
     * 怪物放技能(平A也算在内)
     *
     * @param monster
     * @param target
     * @param skillId
     */
    protected void monsterCastSkill(AbstractMonster monster, Performer target, int skillId) {
        // 普通攻击
        //        FightManager.getInstance().castSkill(monster, skillId, 0, 0, target.getId());
    }

    protected int findSkill(AbstractMonster monster, Performer target) {
        int dis = GeomUtil.distance(monster.getPoint(), target.getPoint());
        if (dis <= 1) {
            return SkillConst.Attack;
        }
        return 0;
    }
}
