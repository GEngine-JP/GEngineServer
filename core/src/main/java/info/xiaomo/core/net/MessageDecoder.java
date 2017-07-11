package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
	
	private MessagePool msgPool;
	

	/**
	 *@param maxFrameLength  最大长度
	 * @param lengthFieldOffset 长度字段的偏移位置
	 * @param lengthFieldLength 长度字段的长度
	 * @param lengthAdjustment 长度调整，比如说 -2 那么实际上长度 就变成了 消息中的长度  - (-2)  
	 * @param initialBytesToStrip 解码返回的byte中，需要跳过的字节数，比如说可以设置跳过头部信息
	 * @throws IOException
	 */
	private MessageDecoder(MessagePool msgPool, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			 int lengthAdjustment, int initialBytesToStrip) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		this.msgPool = msgPool;
	}
	
	public MessageDecoder(MessagePool msgPool) throws IOException {
		this(msgPool, 1024 * 1024, 0, 4, -4, 0);
	}

	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		try {
			
			final int length = frame.readInt();
			
			// 消息
			final int id = frame.readInt();
			
			final short sequence = frame.readShort();
			
			
			Message message = msgPool.get(id);
			if(message == null){
				LOGGER.error("未注册的消息,id:" + id);
				return null;
			}
			
			byte[] bytes = null;
			int remainLength = frame.readableBytes();
			if(remainLength > 0){
				bytes = new byte[remainLength];
				frame.readBytes(bytes);
			}
			
			message.setLength(length);
			message.setSequence(sequence);
			if(bytes != null){
				message.decode(bytes);
			}
			LOGGER.debug("解析消息:" + message);
			return message;
			
		} catch (Exception e) {
			LOGGER.error(ctx.channel() + "消息解码异常", e);
			return null;
		} finally{
			if(frame != null) {
				frame.release();
			}
		}
	}
	
}
