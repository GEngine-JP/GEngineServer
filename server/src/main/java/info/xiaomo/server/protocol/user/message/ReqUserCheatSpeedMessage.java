package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;


/**
 * 玩家使用加速器记录
 */
public class ReqUserCheatSpeedMessage extends AbstractMessage {

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
	}

	@Override
	public byte[] getContent() {
		return new byte[0];
	}

	@Override
	public int getId() {
		return 101120;
	}

}

