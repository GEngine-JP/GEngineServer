/**
 * 工具生成，请遵循规范<br>
 *
 * @auther JiangZhiYong
 */
package info.xiaomo.server.fishscript.room.handler;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.gameserver.room.QuitRoomRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@HandlerEntity(mid = MsgId.QuitRoomReq_VALUE, msg = QuitRoomRequest.class)
public class QuitRoomHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuitRoomHandler.class);

	@Override
	public void run() {
		QuitRoomRequest request = getMsg();
	}
}