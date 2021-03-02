package info.xiaomo.server.rpg.fsm.player;

import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.map.Player;

public class ActiveState extends FSMState<Player> {

    public ActiveState(int type, Player performer) {
        super(type, performer);
    }

    @Override
    public void enter(AbstractGameMap map) {}

    @Override
    public void exit(AbstractGameMap map) {}

    @Override
    public void update(AbstractGameMap map, int delta) {

        performer.getMachine().getAiData().updateMyTargetOrAttackedMe(delta);
    }

    @Override
    public int checkTransition(AbstractGameMap map) {

        if (this.performer.isDead()) {
            return Die;
        }
        if (this.performer.getThreatMap().isEmpty()) {
            // 仇恨列表为空进入活跃状态
            return Active;
        } else {
            // 否则进入战斗状态
            return Fight;
        }
    }
}
