package info.xiaomo.server.rpg.fsm.monster.state;

import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.ai.AIFactory;

public class DieState extends FSMState<Monster> {

    public DieState(int type, Monster performer) {
        super(type, performer);
    }

    @Override
    public void enter(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.dieEnter(map, performer);
    }

    @Override
    public void exit(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.dieExit(map, performer);
    }

    @Override
    public void update(AbstractGameMap map, int delta) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.dieUpdate(map, performer, delta);
    }

    @Override
    public int checkTransition(AbstractGameMap map) {
        int dieDelay = performer.getDieDelay();
        if (dieDelay == -1) // 停留在死亡状态
        return Die;

        if (dieDelay < 1) { // 至少停留一秒的死亡时间
            dieDelay = 1;
        }
        if (!performer.isDead()) {
            return Active;
        }
        if (System.currentTimeMillis() - performer.getMachine().getAiData().getDieTime()
                < dieDelay * 1000L) {
            return Die;
        } else {
            return Sleep;
        }
    }
}
