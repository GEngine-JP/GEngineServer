package info.xiaomo.server.rpg.fsm.monster.state;

import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.ai.AIFactory;

public class ActiveState extends FSMState<Monster> {

    public ActiveState(int type, Monster performer) {
        super(type, performer);
    }

    @Override
    public void enter(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.activeEnter(map, performer);
    }

    @Override
    public void exit(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.activeExit(map, performer);
    }

    @Override
    public void update(AbstractGameMap map, int delta) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.activeUpdate(map, performer, delta);
    }

    @Override
    public int checkTransition(AbstractGameMap map) {
        if (performer.isDead()) { // 瞬秒（还没来得及进入战斗状态）
            return Fight;
        }

        if (!performer.getThreatMap().isEmpty()) {
            return Fight;
        }

        return Active;
    }
}
