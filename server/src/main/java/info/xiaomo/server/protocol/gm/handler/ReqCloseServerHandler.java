package info.xiaomo.server.protocol.gm.handler;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.server.back.BackManager;

public class ReqCloseServerHandler extends AbstractHandler {

    @Override
    protected void execute() {

        try {
            BackManager.getInstance().closeServer();
        } catch (Exception e) {
            LOGGER.error("", e);
        }

    }
}

