package info.xiaomo.server.protocol.gm.handler;

import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.Session;
import info.xiaomo.gameCore.base.AbstractHandler;

import info.xiaomo.server.protocol.gm.message.ReqCloseServerMessage;

public class ReqCloseServerHandler extends AbstractHandler {

	@Override
	protected void execute() {

		try {
//			Session session = (Session) this.getParam();
//			ReqCloseServerMessage msg = (ReqCloseServerMessage) this.getMessage();
			BackManager.getInstance().closeServer();
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}
}

