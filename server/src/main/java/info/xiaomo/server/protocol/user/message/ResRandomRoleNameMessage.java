package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.RandomRoleNameResponse;

/**
 * 返回随机名字
 */
public class ResRandomRoleNameMessage extends AbstractMessage {

	private RandomRoleNameResponse randomRoleNameResponse;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.randomRoleNameResponse = RandomRoleNameResponse.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return randomRoleNameResponse.toByteArray();
	}

	@Override
	public int getId() {
		return 101106;
	}

	public RandomRoleNameResponse getRandomRoleNameResponse() {
		return randomRoleNameResponse;
	}

	public void setRandomRoleNameResponse(RandomRoleNameResponse randomRoleNameResponse) {
		this.randomRoleNameResponse = randomRoleNameResponse;
	}

}

