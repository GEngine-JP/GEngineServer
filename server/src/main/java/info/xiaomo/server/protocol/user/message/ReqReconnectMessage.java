package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.ReconnectRequest;

/**
 * 重新连接请求
 */
public class ReqReconnectMessage extends AbstractMessage {

	private ReconnectRequest reconnectRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.reconnectRequest = ReconnectRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return reconnectRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101116;
	}

	public ReconnectRequest getReconnectRequest() {
		return reconnectRequest;
	}

	public void setReconnectRequest(ReconnectRequest reconnectRequest) {
		this.reconnectRequest = reconnectRequest;
	}

}

