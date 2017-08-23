package info.xiaomo.server.protocol.user;

import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.protocol.proto.UserProto.LoginRequest;
import com.google.protobuf.InvalidProtocolBufferException;

public class ReqLoginMessage extends AbstractMessage {

    private LoginRequest req;

    @Override
    public void doAction() {

    }

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException{
        this.req = LoginRequest.parseFrom(bytes);
	}

    @Override
    public int getId() {
        return 101;
    }

}

