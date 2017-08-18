package info.xiaomo.server.protocol.user.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

import info.xiaomo.server.protocol.proto.UserProto.UserFeedbackRequest;

/**
 * 用户反馈请求
 */
public class ReqUserFeedbackMessage extends AbstractMessage {

	private UserFeedbackRequest userFeedbackRequest;

	@Override
	public void decode(byte[] bytes) throws InvalidProtocolBufferException {
		this.userFeedbackRequest = UserFeedbackRequest.parseFrom(bytes);
	}

	@Override
	public byte[] getContent() {
		return userFeedbackRequest.toByteArray();
	}

	@Override
	public int getId() {
		return 101118;
	}

	public UserFeedbackRequest getUserFeedbackRequest() {
		return userFeedbackRequest;
	}

	public void setUserFeedbackRequest(UserFeedbackRequest userFeedbackRequest) {
		this.userFeedbackRequest = userFeedbackRequest;
	}

}

