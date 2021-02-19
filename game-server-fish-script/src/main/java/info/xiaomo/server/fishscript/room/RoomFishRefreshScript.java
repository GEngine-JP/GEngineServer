package info.xiaomo.server.fishscript.room;

import java.util.ArrayList;
import java.util.function.Consumer;
import info.xiaomo.gengine.math.MathUtil;
import info.xiaomo.server.fish.manager.RoomManager;
import info.xiaomo.server.fish.script.IFishScript;
import info.xiaomo.server.shared.entity.Fish;
import info.xiaomo.server.shared.entity.room.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 刷新房间鱼群
 * <p>
 * <p>
 * 2017年9月14日 上午9:39:23
 */
public class RoomFishRefreshScript implements IFishScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoomFishRefreshScript.class);

	@Override
	public void fishRefresh(Room iRoom) {
//		LOGGER.warn("刷新鱼");
		Fish fish1 = bornFish(iRoom, MathUtil.random(1, 5), null);
		Fish fish2 = bornFish(iRoom, MathUtil.random(1, 5), null);
		Fish fish3 = bornFish(iRoom, MathUtil.random(1, 5), null);
		Fish fish4 = bornFish(iRoom, MathUtil.random(1, 5), null);
		Fish fish5 = bornFish(iRoom, MathUtil.random(1, 5), null);

		RoomManager.getInstance().broadcastFishEnter(iRoom, fish1, fish2, fish3, fish4, fish5/*,fish1,fish2,fish3,fish4,fish5*/);

	}

	/**
	 * 出生鱼
	 * <p>
	 * <p>
	 * 2017年9月25日 下午4:00:39
	 *
	 * @param configId
	 * @return
	 */
	private Fish bornFish(Room room, int configId, Consumer<Fish> fishConsumer) {
		Fish fish = new Fish();
		fish.setConfigId(configId);

		if (fishConsumer != null) {
			fishConsumer.accept(fish);
		}
		fish.setRoom(room);
		fish.setTrackIds(new ArrayList<Integer>());
		room.getFishMap().put(fish.getId(), fish);
		return fish;
	}

}
