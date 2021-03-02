package info.xiaomo.server.rpg.fsm.monster.ai;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.ai.fsm.AIType;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.server.rpg.fsm.monster.ai.impl.ActiveAI;
import info.xiaomo.server.rpg.fsm.monster.ai.impl.PassiveAI;
import info.xiaomo.server.rpg.fsm.monster.ai.impl.SkillMonsterAI;
import info.xiaomo.server.rpg.fsm.monster.ai.impl.StaticAI;

/** Date : 17/6/29 10:28 desc : Copyright(©) 2017 by xiaomo. */
public class AIFactory {

    private static final Map<Integer, MonsterAI> AI_MAP = new HashMap<>();

    static {
        AI_MAP.put(AIType.NONE, new StaticAI());
        AI_MAP.put(AIType.Threat_ACTIVE, new ActiveAI());
        AI_MAP.put(AIType.THREAT_PASSIVE, new PassiveAI());
        AI_MAP.put(AIType.SKILL_MONSTER, new SkillMonsterAI());
    }

    public static MonsterAI getAI(int type) {
        return AI_MAP.get(type);
    }
}
