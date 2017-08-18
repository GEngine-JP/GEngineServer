package info.xiaomo.server.protocol.user.handler;

import info.xiaomo.server.server.Session;
import info.xiaomo.gameCore.base.AbstractHandler;

import info.xiaomo.server.protocol.user.message.ReqUserCheatSpeedMessage;

public class ReqUserCheatSpeedHandler extends AbstractHandler {

	@Override
	protected void execute() {

		try {
			Session session = (Session) this.getParam();
			ReqUserCheatSpeedMessage msg = (ReqUserCheatSpeedMessage) this.getMessage();
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}
}

