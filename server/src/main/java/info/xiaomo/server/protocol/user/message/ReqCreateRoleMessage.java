package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.CreateRoleRequest;

/**
 * 创建角色
 */
public class ReqCreateRoleMessage extends AbstractMessage {

	private CreateRoleRequest createRoleRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.createRoleRequest = CreateRoleRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return createRoleRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101104;
	}

	public CreateRoleRequest getCreateRoleRequest() {
		return createRoleRequest;
	}

	public void setCreateRoleRequest(CreateRoleRequest createRoleRequest) {
		this.createRoleRequest = createRoleRequest;
	}

}

