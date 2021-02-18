package info.xiaomo.server.fishscript.server.handler;

import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.server.ChangeRoleServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 改变服务器结果
 * 
 *
 *  2017年8月3日 下午4:11:03
 */
@HandlerEntity(mid = MsgId.ChangeRoleServerRes_VALUE, msg = ChangeRoleServerResponse.class)
public class ChangeRoleServerResHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangeRoleServerResHandler.class);

	@Override
	public void run() {
		ChangeRoleServerResponse res = getMsg();
		UserRole role = RoleManager.getInstance().getRole(rid);
		if (res.getResult() == 0 && role != null) {
			RoleManager.getInstance().quit(role, GlobalReason.CrossServer);
		}

		LOGGER.info("角色{}跨服退出{}", rid, res.getResult());

	}

}
