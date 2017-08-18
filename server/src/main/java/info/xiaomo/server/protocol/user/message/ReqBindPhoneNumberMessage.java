package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.BindPhoneNumberRequest;

/**
 * 绑定手机号请求
 */
public class ReqBindPhoneNumberMessage extends AbstractMessage {

	private BindPhoneNumberRequest bindPhoneNumberRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.bindPhoneNumberRequest = BindPhoneNumberRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return bindPhoneNumberRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101119;
	}

	public BindPhoneNumberRequest getBindPhoneNumberRequest() {
		return bindPhoneNumberRequest;
	}

	public void setBindPhoneNumberRequest(BindPhoneNumberRequest bindPhoneNumberRequest) {
		this.bindPhoneNumberRequest = bindPhoneNumberRequest;
	}

}

