package info.xiaomo.server.protocol.message.gm;

import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.AbstractMessage;

public class ReqCloseServerMessage extends AbstractMessage {

    public ReqCloseServerMessage() {
        this.queueId = 2;
    }


    @Override
    public void doAction() {
        BackManager.getInstance().closeServer();
    }

    @Override
    public int getId() {
        return 201;
    }

}

