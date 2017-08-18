package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.DisconnectResponse;

/**
 * 断线响应
 */
public class ResDisconnectMessage extends AbstractMessage {

	private DisconnectResponse disconnectResponse;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.disconnectResponse = DisconnectResponse.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return disconnectResponse.toByteArray();
	}

	@Override
	public int getId() {
		return 101117;
	}

	public DisconnectResponse getDisconnectResponse() {
		return disconnectResponse;
	}

	public void setDisconnectResponse(DisconnectResponse disconnectResponse) {
		this.disconnectResponse = disconnectResponse;
	}

}

