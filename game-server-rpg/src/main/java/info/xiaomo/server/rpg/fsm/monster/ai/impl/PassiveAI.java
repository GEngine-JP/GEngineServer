package info.xiaomo.server.rpg.fsm.monster.ai.impl;

import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterAIUtil;

/**
 * 被动怪物
 *
 * @author zhangli 2017年6月6日 下午9:56:27
 */
public class PassiveAI extends ActiveAI {
    /**
     * 更新活跃状态
     *
     * @param map
     * @param monster
     * @param dt
     * @return
     */
    @Override
    public boolean activeUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        AIData aiData = monster.getMachine().getAiData();
        aiData.updateActiveAITime(dt);
        // 更新血量
        MonsterAIUtil.recoverHp((Monster) monster, RECOVER_HP_TIME_INTERVAL);

        MapPoint birth = monster.getBirthPoint();
        MapPoint cur = monster.getPoint();
        int disToBirth = GeomUtil.distance(birth, cur);
        MonsterAIUtil.backHome((Monster) monster, disToBirth, dt);
        int moveInterval = Math.max(monster.getMoveInterval(), 400);
        aiData.setSleepTime(moveInterval);
        return true;
    }
}
