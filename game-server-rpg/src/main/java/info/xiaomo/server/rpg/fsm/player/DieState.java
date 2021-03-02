package info.xiaomo.server.rpg.fsm.player;

import info.xiaomo.gengine.ai.fsm.FSMState;
import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.server.rpg.config.ConfigDataManager;
import info.xiaomo.server.rpg.config.tables.MapConfig;
import info.xiaomo.server.rpg.event.EventType;
import info.xiaomo.server.rpg.map.Player;
import info.xiaomo.server.rpg.system.player.PlayerManager;

public class DieState extends FSMState<Player> {

	public DieState(int type, Player performer) {
		super(type, performer);
	}

	@Override
	public void enter(AbstractGameMap map) {
		this.performer.setReliveHere(false);
		this.performer.setReliveHereTime(0);
		this.performer.setDeadTime(System.currentTimeMillis());
		
		
		MapConfig config = ConfigDataManager.getInstance().getConfig(MapConfig.class, map.getCfgId());
		this.performer.setReliveHomeTime(config.getReliveHomeTime() * 1000);
		
		
		performer.getMachine().getAiData().clearMyTargetOrAttackedMe();
		PlayerManager.getInstance().playerDie(map, performer);
		EventUtil.fireEvent(EventType.PLAYER_DIE, this.performer);
	}

	@Override
	public void exit(AbstractGameMap map) {

//		PlayerManager.getInstance().relive(this.performer);
//		if(!this.performer.isReliveHere()){
//			//非原地复活，都要回到复活点
//			PlayerManager.getInstance().deliverHome(map, this.performer);
//		}
	}

	@Override
	public void update(AbstractGameMap map, int delta) {
		super.update(map, delta);
		
		if(this.performer.isReliveHere()) {
			this.performer.setReliveHereTime(this.performer.getReliveHereTime() - delta);
		} else {
			this.performer.setReliveHomeTime(this.performer.getReliveHomeTime() - delta);
		}
		
	}

	@Override
	public int checkTransition(AbstractGameMap map) {
		
		if(!this.performer.isDead()){
			return Active;
		} 
		
		if(this.performer.isReliveHere()) { 
			//选择原地复活
			if(this.performer.getReliveHereTime() <= 0){
				return Active;
			}
		} else {
			if(this.performer.getReliveHomeTime() <= 0) {
				return Active;
			}
		}
		
		return Die;
		
	}

}
