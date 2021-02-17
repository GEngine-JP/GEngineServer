/**工具生成，请遵循规范<br>
 @auther JiangZhiYong
 */
package info.xiaomo.server.gameserverscript.bydr.tcp.fight;


import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.gameserver.fight.GunLeveUpRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HandlerEntity(mid= MsgId.GunLeveUpReq_VALUE,msg= GunLeveUpRequest.class)
public class GunLeveUpHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GunLeveUpHandler.class);

	@Override
	public void run() {
		GunLeveUpRequest request = getMsg();
	}
}