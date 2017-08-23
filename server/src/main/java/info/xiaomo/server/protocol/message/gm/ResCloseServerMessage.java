package info.xiaomo.server.protocol.message.gm;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.server.protocol.proto.GMProto.CloseServerResponse;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 关服返回
 */
public class ResCloseServerMessage extends AbstractMessage {

    private CloseServerResponse closeServerResponse;

    @Override
    public void decode(byte[] bytes) throws InvalidProtocolBufferException {
        this.closeServerResponse = CloseServerResponse.parseFrom(bytes);
    }


    @Override
    public int getId() {
        return 2202;
    }

    public CloseServerResponse getCloseServerResponse() {
        return closeServerResponse;
    }

    public void setCloseServerResponse(CloseServerResponse closeServerResponse) {
        this.closeServerResponse = closeServerResponse;
    }

}

