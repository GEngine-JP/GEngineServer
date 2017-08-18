package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.LoginResponse;

/**
 * 登录返回
 */
public class ResLoginMessage extends AbstractMessage {

	private LoginResponse loginResponse;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.loginResponse = LoginResponse.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return loginResponse.toByteArray();
	}

	@Override
	public int getId() {
		return 101102;
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	public void setLoginResponse(LoginResponse loginResponse) {
		this.loginResponse = loginResponse;
	}

}

