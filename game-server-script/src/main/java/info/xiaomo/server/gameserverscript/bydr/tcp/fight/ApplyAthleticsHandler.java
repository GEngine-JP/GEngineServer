package info.xiaomo.server.gameserverscript.bydr.tcp.fight;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubSubMessage;
import info.xiaomo.server.gameserver.world.BydrWorldChannel;
import info.xiaomo.server.shared.protocol.gameserver.room.ApplyAthleticsRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 报名竞技赛
 *
 *
 *  2017年8月3日 上午9:23:04
 */
@HandlerEntity(mid = MsgId.ApplyAthleticsReq_VALUE, msg = ApplyAthleticsRequest.class)
public class ApplyAthleticsHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyAthleticsHandler.class);

	@Override
	public void run() {
		ApplyAthleticsRequest req = getMsg();
		LOGGER.info("{}参加竞技赛", rid);
		JedisPubSubMessage msg = new JedisPubSubMessage(rid, req.getType().getNumber(), req.getRank());
		JedisManager.getJedisCluster().publish(BydrWorldChannel.ApplyAthleticsReq.toString(), msg.toString());
	}

}
