package info.xiaomo.server.protocol.user.handler;

import info.xiaomo.server.server.Session;
import info.xiaomo.gameCore.base.AbstractHandler;

import info.xiaomo.server.protocol.user.message.ReqBackToChooseRoleMessage;

public class ReqBackToChooseRoleHandler extends AbstractHandler {

	@Override
	protected void execute() {

		try {
			Session session = (Session) this.getParam();
			ReqBackToChooseRoleMessage msg = (ReqBackToChooseRoleMessage) this.getMessage();
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}
}

