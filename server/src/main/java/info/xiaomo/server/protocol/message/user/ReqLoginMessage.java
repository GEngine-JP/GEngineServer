package info.xiaomo.server.protocol.message.user;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.server.protocol.proto.UserProto.LoginRequest;
import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.user.UserManager;

public class ReqLoginMessage extends AbstractMessage {

    private LoginRequest req;

    public ReqLoginMessage() {
        this.queueId = 1;
    }

    @Override
    public void doAction() {
        UserManager.getInstance().login(session, req.getLoginName());
    }

    @Override
    public void decode(byte[] bytes) throws InvalidProtocolBufferException {
        this.req = LoginRequest.parseFrom(bytes);
    }

    @Override
    public int getId() {
        return 101101;
    }


}

