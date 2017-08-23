package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.server.server.MessageFilter;
import info.xiaomo.server.server.MessageProcessor;
import info.xiaomo.server.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 地图消息处理器
 *
 * @author zhangli
 * 2017年6月6日 下午9:34:00
 */
public class GameMapProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMapProcessor.class);


    @Override
    public void process(IQueueDriverCommand message) {

        Session session = (Session) message.getParam();

//        Player player = session.getPlayer();

//		GameMap map = MapManager.getInstance().getMap(player.getMapId(), player.getLine());
//
//		if (map == null) { // 找不到地图
//			return;
//		}
//		// 设置新的队列id为玩家地图id
//		// 此处是为了防止
//		message.setQueueId(player.getMapId());
//
//		map.getDriver().addCommand(message);

    }


    class GameMapMessageFilter implements MessageFilter {

        @Override
        public boolean before(Message msg) {
//            Player player = msg.getParam().getPlayer();
//            if (msg.getQueueId() != player.getMapId()) {
//                LOGGER.error("玩家当前队列id和消息的队列id已经不一样，丢弃消息[old:{},new:{}]", msg.getQueueId(), player.getMapId());
//                return false;
//            }
            return true;
        }

        @Override
        public boolean after(Message msg) {
            return false;
        }

    }

}
