package info.xiaomo.server.protocol.gm.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.GMProto.CloseServerRequest;

/**
 * 关服请求
 */
public class ReqCloseServerMessage extends AbstractMessage {

	private CloseServerRequest closeServerRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.closeServerRequest = CloseServerRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return closeServerRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 2201;
	}

	public CloseServerRequest getCloseServerRequest() {
		return closeServerRequest;
	}

	public void setCloseServerRequest(CloseServerRequest closeServerRequest) {
		this.closeServerRequest = closeServerRequest;
	}

}

