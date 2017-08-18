package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.RoleIdMsg;

/**
 * 选择角色进入游戏
 */
public class ReqChooseRoleMessage extends AbstractMessage {

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
		return 101107;
	}

	public RoleIdMsg getRoleIdMsg() {
		return roleIdMsg;
	}

	public void setRoleIdMsg(RoleIdMsg roleIdMsg) {
		this.roleIdMsg = roleIdMsg;
	}

}

