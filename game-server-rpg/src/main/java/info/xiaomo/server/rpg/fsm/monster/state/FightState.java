package info.xiaomo.server.rpg.fsm.monster.state;


import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.ai.fsm.monster.MonsterAI;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.fsm.monster.Monster;
import info.xiaomo.server.rpg.fsm.monster.ai.AIFactory;

public class FightState extends FSMState<Monster> {

	public FightState(int type, Monster performer) {
		super(type, performer);
	}

	@Override
	public void enter(AbstractGameMap map) {
		// 进入战斗状态，清除战斗记录
		int aiType = performer.getAiType();
		MonsterAI monsterAI = AIFactory.getAI(aiType);
		monsterAI.battleEnter(map, performer);
	}

	@Override
	public void exit(AbstractGameMap map) {
		int aiType = performer.getAiType();
		MonsterAI monsterAI = AIFactory.getAI(aiType);
		monsterAI.battleExit(map, performer);
	}

	@Override
	public void update(AbstractGameMap map, int delta) {
		int aiType = performer.getAiType();
		MonsterAI monsterAI = AIFactory.getAI(aiType);
		monsterAI.battleUpdate(map, performer, delta);
	}

	@Override
	public int checkTransition(AbstractGameMap map) {

		if (performer.isDead()) {
			return Die;
		}

		if (performer.getFightTarget() > 0) {
			// 仇恨列表不为空，继续停留在战斗状态下
			return Fight;
		}
		if (performer.getThreatMap().size() > 0) {
			return Fight;
		}
		// 否则进入活跃状态
		return Active;
	}

}
