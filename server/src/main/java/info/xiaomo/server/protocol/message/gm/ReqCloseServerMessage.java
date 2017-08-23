package info.xiaomo.server.protocol.message.gm;

import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.protocol.proto.GMProto.CloseServerRequest;
import com.google.protobuf.InvalidProtocolBufferException;

public class ReqCloseServerMessage extends AbstractMessage {

    private CloseServerRequest req;

    @Override
    public void doAction() {

    }

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException{
        this.req = CloseServerRequest.parseFrom(bytes);
	}

    @Override
    public int getId() {
        return 201;
    }



}

