package info.xiaomo.server.protocol.user;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.protocol.proto.UserProto.LoginResponse;

/**
 * 登录返回
 */
public class ResLoginMessage extends AbstractMessage {

	private LoginResponse res;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.res = LoginResponse.parseFrom(bytes);
	}

	@Override
	public int getId() {
		return 1102;
	}

	public LoginResponse getLoginResponse() {
		return res;
	}

	public void setLoginResponse(LoginResponse res) {
		this.res = res;
	}

}

