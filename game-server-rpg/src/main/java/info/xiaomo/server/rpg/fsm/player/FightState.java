package info.xiaomo.server.rpg.fsm.player;

import java.util.Map;
import info.xiaomo.gengine.ai.fsm.AIData;
import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.map.Player;

public class FightState extends FSMState<Player> {

	public FightState(int type, Player performer) {
		super(type, performer);
	}

	@Override
	public void enter(AbstractGameMap map) {
		super.enter(map);
		
	}

	@Override
	public void exit(AbstractGameMap map) {
		super.exit(map);
	}

	@Override
	public void update(AbstractGameMap map, int delta) {
		
		if(this.performer.isDead()) {
			return;
		}
		
		performer.getMachine().getAiData().updateMyTargetOrAttackedMe(delta);
        //TODO 回血和内力
		clearThreat(delta);
	}

	@Override
	public int checkTransition(AbstractGameMap map) {
		if (performer.isDead()) {// 瞬秒（还没来得及进入战斗状态）
			return Die;
		}
		if (!performer.getThreatMap().isEmpty()) {
			return Fight;
		}
		return Active;
	}

	private void clearThreat(int dt) {
		AIData aiData = performer.getMachine().getAiData();
		int clearTime = aiData.getClearThreatTime();
		if (clearTime % 10000 < dt) {
			Map<Long, Integer> threatMap = performer.getThreatMap();
			for (Long key : threatMap.keySet()) {
				Integer value = threatMap.get(key);
				if (value != null) {
					if (value > 0) {
						threatMap.put(key, 1);
					}
				}
			}

		} else if (clearTime % 5000 < dt) {
			boolean isallempty = true;

			Map<Long, Integer> threatMap = performer.getThreatMap();

			for (Long key : threatMap.keySet()) {
				int value = threatMap.get(key);
				if (value > 1) {
					isallempty = false;
				} else {
					threatMap.remove(key);
				}
			}

			if (isallempty) {
				threatMap.clear();
			}
		}
		if (clearTime < dt) {
			clearTime = clearTime + 60000;
			aiData.setClearThreatTime(clearTime);
		}
		
	}

}
