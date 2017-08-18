package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.RoleIdMsg;

/**
 * 删除角色请求
 */
public class ReqDeleteRoleMessage extends AbstractMessage {

	private RoleIdMsg roleIdMsg;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.roleIdMsg = RoleIdMsg.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return roleIdMsg.toByteArray();
	}

	@Override
	public int getId() {
		return 101109;
	}

	public RoleIdMsg getRoleIdMsg() {
		return roleIdMsg;
	}

	public void setRoleIdMsg(RoleIdMsg roleIdMsg) {
		this.roleIdMsg = roleIdMsg;
	}

}

