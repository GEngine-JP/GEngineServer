package info.xiaomo.server.protocol.gm.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.GMProto.CloseServerResponse;

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
	public byte[] getContent() {
		return closeServerResponse.toByteArray();
	}

	@Override
	public int getId() {
		return 201202;
	}

	public CloseServerResponse getCloseServerResponse() {
		return closeServerResponse;
	}

	public void setCloseServerResponse(CloseServerResponse closeServerResponse) {
		this.closeServerResponse = closeServerResponse;
	}

}

