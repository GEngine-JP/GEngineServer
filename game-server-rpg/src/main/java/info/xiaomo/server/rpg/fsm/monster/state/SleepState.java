package info.xiaomo.server.rpg.fsm.monster.state;

import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.MonsterConst;
import info.xiaomo.server.rpg.fsm.monster.ai.AIFactory;

public class SleepState extends FSMState<Monster> {

    public SleepState(int type, Monster performer) {
        super(type, performer);
    }

    @Override
    public void enter(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.sleepEnter(map, performer);
    }

    @Override
    public void exit(AbstractGameMap map) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.sleepExit(map, performer);
    }

    @Override
    public void update(AbstractGameMap map, int delta) {
        int aiType = performer.getAiType();
        MonsterAI monsterAI = AIFactory.getAI(aiType);
        monsterAI.sleepUpdate(map, performer, delta);
    }

    @Override
    public int checkTransition(AbstractGameMap map) {
        // 主要是检查复活时间
        long currentTime = System.currentTimeMillis();
        AIData aiData = performer.getMachine().getAiData();

        if (performer.isInit()) { // 刚初始化的对象
            performer.setInit(false);
            return Active;
        }

        int reliveType = performer.getReliveType();

        if (reliveType == MonsterConst.MonsterReliveType.FIX_TIME) {
            if (currentTime >= aiData.getNextReliveTime()) {
                return Active;
            }
        }

        return Sleep;
    }

    @Override
    public void copyTime(int sleepTime) {
        super.copyTime(sleepTime);
    }

    @Override
    public int getTime() {
        return super.getTime();
    }
}
