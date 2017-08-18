package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;


/**
 * 返回选角色界面
 */
public class ReqBackToChooseRoleMessage extends AbstractMessage {

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
	}

	@Override
	public byte[] getContent() {
		return new byte[0];
	}

	@Override
	public int getId() {
		return 101113;
	}

}

