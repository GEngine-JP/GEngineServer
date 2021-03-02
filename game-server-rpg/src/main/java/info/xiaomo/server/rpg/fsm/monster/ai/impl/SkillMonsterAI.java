package info.xiaomo.server.rpg.fsm.monster.ai.impl;

import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterAIUtil;

/**
 * 有技能的怪物
 *
 * @author zhangli 2017年6月6日 下午9:56:34
 */
public class SkillMonsterAI extends ActiveAI {

    @Override
    protected int findSkill(AbstractMonster monster, Performer target) {
        return MonsterAIUtil.findSkill((Monster) monster, target);
    }
}
