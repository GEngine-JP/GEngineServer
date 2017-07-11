package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class MessagePackage {

	public static ByteBuf packageMsg(Message msg) {

		byte[] content = msg.encode();
		
		// 长度字段4 + id字段4 + seq2 + 正文
		int length = 10 + content.length;
		
		msg.setLength(length);
		
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
		
		msg.setLength(length);
		
		// 写入消息头
		buffer.writeInt(length);
		buffer.writeInt(msg.getId());
		buffer.writeShort(msg.getSequence());
		buffer.writeBytes(content);
		return buffer;
	}
	
	public static ByteBuf packageMsgGroup(Message msg) {

		byte[] content = msg.encode();
		
		int length = content.length;
		msg.setLength(length);
		
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
		
		//消息组是打包好的消息集合，没有消息头，直接写入buffer
		buffer.writeBytes(content);
		return buffer;
	}
}
