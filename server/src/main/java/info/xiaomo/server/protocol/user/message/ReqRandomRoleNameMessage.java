package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.RandomRoleNameRequest;

/**
 * 请求随机名字
 */
public class ReqRandomRoleNameMessage extends AbstractMessage {

	private RandomRoleNameRequest randomRoleNameRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.randomRoleNameRequest = RandomRoleNameRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return randomRoleNameRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101105;
	}

	public RandomRoleNameRequest getRandomRoleNameRequest() {
		return randomRoleNameRequest;
	}

	public void setRandomRoleNameRequest(RandomRoleNameRequest randomRoleNameRequest) {
		this.randomRoleNameRequest = randomRoleNameRequest;
	}

}

