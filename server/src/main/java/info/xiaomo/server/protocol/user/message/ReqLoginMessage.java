package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.LoginRequest;

/**
 * 登录请求
 */
public class ReqLoginMessage extends AbstractMessage {

	private LoginRequest loginRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.loginRequest = LoginRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return loginRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101101;
	}

	public LoginRequest getLoginRequest() {
		return loginRequest;
	}

	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}

}

