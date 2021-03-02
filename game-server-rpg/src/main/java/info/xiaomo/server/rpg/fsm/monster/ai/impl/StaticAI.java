package info.xiaomo.server.rpg.fsm.monster.ai.impl;

import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.server.rpg.fsm.monster.ai.AbstractMonsterAI;

/** 静态怪没有战斗状态 宝箱 树 采集怪 等等 */
public class StaticAI extends AbstractMonsterAI {

    // 没有战斗状态
    @Override
    public boolean battleEnter(AbstractGameMap map, AbstractMonster monster) {
        return false;
    }

    @Override
    public boolean battleExit(AbstractGameMap map, AbstractMonster monster) {
        return false;
    }

    @Override
    public boolean battleUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        return false;
    }

    // 没有活跃状态
    @Override
    public boolean activeEnter(AbstractGameMap map, AbstractMonster monster) {
        return false;
    }

    @Override
    public boolean activeUpdate(AbstractGameMap map, AbstractMonster monster, int dt) {
        return false;
    }

    @Override
    public boolean activeExit(AbstractGameMap map, AbstractMonster monster) {
        return false;
    }
}
