package info.xiaomo.server.protocol.user.handler;

import info.xiaomo.server.server.Session;
import info.xiaomo.gameCore.base.AbstractHandler;

import info.xiaomo.server.protocol.user.message.ReqCreateRoleMessage;

public class ReqCreateRoleHandler extends AbstractHandler {

	@Override
	protected void execute() {

		try {
			Session session = (Session) this.getParam();
			ReqCreateRoleMessage msg = (ReqCreateRoleMessage) this.getMessage();
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}
}

