package info.xiaomo.server.rpg.fsm.monster.ai;

import java.util.Map;
import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.server.rpg.event.EventType;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterAIUtil;
import info.xiaomo.server.rpg.fsm.monster.MonsterConst;
import info.xiaomo.server.rpg.map.GameMap;
import info.xiaomo.server.rpg.map.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的怪物ai，主要是提供了一些简单的设想，比如说更新血量，检查时间等等
 *
 * @author zhangli 2017年6月6日 下午9:56:45
 */
public class AbstractMonsterAI implements MonsterAI {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractMonsterAI.class);

    /** 回血检查间隔 */
    protected static final int RECOVER_HP_TIME_INTERVAL = 3000;

    /**
     * 清除寻路列表
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean activeEnter(AbstractGameMap map, AbstractMonster monster) {
        monster.getPathList().clear();
        return true;
    }

    /**
     * 回血逻辑
     *
     * @param map
     * @param monster
     * @param dt
     * @return
     */
    @Override
    public boolean activeUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        // 更新血量
        AIData aiData = monster.getMachine().getAiData();
        aiData.updateActiveAITime(dt);
        MonsterAIUtil.recoverHp((Monster) monster, RECOVER_HP_TIME_INTERVAL);
        return aiData.getSleepTime() <= 0;
    }

    /**
     * 清空寻路列表
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean activeExit(AbstractGameMap map, AbstractMonster monster) {
        monster.getPathList().clear();
        return true;
    }

    /**
     * 清除战斗记录,重新设置仇恨清除时间
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean battleEnter(AbstractGameMap map, AbstractMonster monster) {
        // 进入战斗状态，清除战斗记录
        monster.setFightTarget(0);

        AIData aiData = monster.getMachine().getAiData();

        aiData.setClearThreatTime(65000);

        return true;
    }

    @Override
    public boolean battleUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {

        if (monster.isDead()) {
            return false;
        }

        AIData aiData = monster.getMachine().getAiData();
        aiData.updateFightAITime(dt);

        // 更新仇恨
        MonsterAIUtil.updateThreat((Monster) monster, dt);
        // 更新血量
        MonsterAIUtil.recoverHp((Monster) monster, RECOVER_HP_TIME_INTERVAL);

        if (aiData.getSleepTime() > 0) {
            return false;
        }

        //        if (!BufferManager.getInstance().canDoing(monster)) {
        //            LOGGER.debug("{}-被麻痹,不能移动", monster.getId());
        //            return false;
        //        }

        // 更新仇恨最高的目标对象
        Performer target;
        int[] targetChoose = monster.getTargetChoose();
        int chooseType = 0;
        int chooseTarget = 0;
        int chooseIntervalTime = 0;
        if (targetChoose.length >= 1) {
            chooseType = targetChoose[0];
        }
        if (chooseType == MonsterConst.TargetChooseType.RANDOM) { // 攻击返回内随机选择一个
            chooseIntervalTime = targetChoose[1];
            target = MonsterAIUtil.findRandomTarget((Monster) monster, chooseIntervalTime);
        } else if (chooseType == MonsterConst.TargetChooseType.NEAR) { // 最近的目标
            target = MonsterAIUtil.findNearestFightAblePerformer((Monster) monster);
        } else if (chooseType == MonsterConst.TargetChooseType.FOLLOW) { // 跟随某个目标（类宠物AI）
            chooseTarget = targetChoose[1];
            target = MonsterAIUtil.findPointedTarget((Monster) monster, chooseTarget);
        } else { // 最高仇恨
            target = MonsterAIUtil.findHighestThreatPerformer(map, monster);
        }
        if (target == null) {
            monster.setFightTarget(0);
            monster.setOwner(0);
            return false;
        }
        monster.setFightTarget(target.getId());

        // 设置归属，并发送消息
        Map<Long, Integer> threatMap = monster.getThreatMap();
        Integer threat = threatMap.get(target.getId());
        monster.setOwner(target.getId());
        if (threat > 5) {
            if (target instanceof Player) {
                //                ResMonsterOwnerChangeMessage msg = new
                // ResMonsterOwnerChangeMessage();
                //                msg.setLid(monster.getId());
                //                msg.setOwnerName(target.getName());
                //                MessageUtil.sendRoundMessage(msg, target);
            }
        }

        return true;
    }

    @Override
    public boolean battleExit(AbstractGameMap map, AbstractMonster monster) {
        return true;
    }

    /**
     * 处理死亡相关逻辑
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean dieEnter(AbstractGameMap map, AbstractMonster monster) {
        // 清空仇恨
        monster.getMachine().getAiData().setDieTime(System.currentTimeMillis());
        monster.setFightTarget(0);

        // 掉落经验
        //        MonsterManager.getInstance().addExpOnMonsterDie(map, monster);
        //        //掉落道具
        //        if (monster.getKillerId() > 0) {
        //            MonsterManager.getInstance().drop(monster);
        //        }

        // 副本怪物死亡事件
        if (((GameMap) map).isInstance()) {
            //            ((GameInstance) map).onMonsterDie(monster);
        }
        EventUtil.fireEvent(EventType.MONSTER_DIE, monster);

        // 清楚仇恨
        //        MonsterManager.getInstance().clearThreat(map, monster);
        return true;
    }

    @Override
    public boolean dieUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        return true;
    }

    @Override
    public boolean dieExit(AbstractGameMap map, AbstractMonster monster) {
        return true;
    }

    /**
     * 处理复活相关
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean sleepEnter(AbstractGameMap map, AbstractMonster monster) {

        // 主要是设置复活时间
        if (monster.isRemoveAfterDie()) {
            map.removeMonster(monster, true);
            return false;
        }

        map.removeView(monster);
        map.stand(monster, null);

        int reliveType = monster.getReliveType();
        AIData aiData = monster.getMachine().getAiData();
        long currentTime = System.currentTimeMillis();
        if (reliveType == MonsterConst.MonsterReliveType.FIX_TIME) {
            aiData.setNextReliveTime(currentTime + monster.getReliveDelay());
        } else if (reliveType == MonsterConst.MonsterReliveType.DAY_TIME) {

        }
        return true;
    }

    @Override
    public boolean sleepUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        return true;
    }

    /**
     * 更新boss 状态 活着还是死了
     *
     * @param map
     * @param monster
     * @return
     */
    @Override
    public boolean sleepExit(AbstractGameMap map, AbstractMonster monster) {
        //        MonsterManager.getInstance().relive(map, monster);
        return true;
    }
}
