/**
 * 工具生成，请遵循规范<br>
 *
 * @auther JiangZhiYong
 */
package info.xiaomo.server.fishscript.fight.handler;


import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.gameserver.fight.GunLeveUpRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;

@HandlerEntity(mid = MsgId.GunLeveUpReq_VALUE, msg = GunLeveUpRequest.class)
public class GunLeveUpHandler extends TcpHandler {
	@Override
	public void run() {
		GunLeveUpRequest request = getMsg();
	}
}