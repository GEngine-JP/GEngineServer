package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import info.xiaomo.server.server.MessageProcessor;
import info.xiaomo.server.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务消息处理器
 *
 * @author zhangli
 * 2017年6月6日 下午9:34:00
 */
public class LogicProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogicProcessor.class);


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

}
